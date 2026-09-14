package com.gpn.crm.order.service;

import com.gpn.crm.keycrm.client.KeyCrmOrderClient;
import com.gpn.crm.keycrm.dto.KeyCrmOrder;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.order.dto.OrderQuery;
import com.gpn.crm.order.mapper.OrderEntityMapper;
import com.gpn.crm.order.model.OrderEntity;
import com.gpn.crm.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * Persists orders fetched from KeyCRM into the local orders/order_products/... tables
 * (V5__add_orders_tables.sql). Re-syncing an order (same KeyCRM id) fully replaces its child
 * rows (products/payments/tags/custom_fields/assignees), since {@code OrderEntityMapper}
 * rebuilds those collections from scratch and {@code OrderEntity}'s cascade + orphanRemoval
 * takes care of inserting/updating/deleting the difference.
 * <p>
 * Neither sync method is wrapped in a Spring {@code @Transactional} at the top level -
 * fetching from KeyCRM is slow (many HTTP round trips, plus the full sync's deliberate
 * throttling sleeps) and holding a DB connection/transaction open for that whole time would
 * risk exhausting the pool. Persistence happens through {@code orderRepository.saveAll(...)},
 * which Spring Data already runs in its own short-lived transaction per call.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSyncService {

    private static final int MAX_PAGE_SIZE = 50;
    /** KeyCRM allows up to 20 requests/second; pause well under that after every batch. */
    private static final int THROTTLE_BATCH_SIZE = 15;
    private static final Duration THROTTLE_PAUSE = Duration.ofSeconds(90);
    /** "Latest orders" quick sync: 10 requests x 50 orders, well under the throttle threshold above - no pausing needed. */
    private static final int LATEST_ORDERS_PAGE_COUNT = 10;

    private final OrderService orderService;
    private final KeyCrmOrderClient orderClient;
    private final OrderEntityMapper orderEntityMapper;
    private final OrderRepository orderRepository;

    /** Date-ranged sync - see {@code OrderController}'s POST /api/orders/sync. */
    public int syncOrders(OrderQuery query) {
        return savePage(orderService.fetchOrders(query));
    }

    /**
     * Full-catalog sync: pages through every order KeyCRM has, oldest first ({@code sort=id}),
     * saving each page as it's fetched rather than accumulating everything in memory - so a
     * late-page failure doesn't lose the progress already made. Stays under KeyCRM's rate limit
     * by pausing {@value THROTTLE_PAUSE} after every {@value THROTTLE_BATCH_SIZE} requests.
     * Intended to run once a day - see {@code OrderSyncScheduler}.
     */
    public int syncAllOrders() {
        int page = 1;
        int lastPage;
        int requestsSincePause = 0;
        int totalSynced = 0;

        do {
            KeyCrmPage<KeyCrmOrder> keyCrmPage = orderClient.getAllOrdersSortedById(page, MAX_PAGE_SIZE);
            totalSynced += savePage(keyCrmPage.data());

            lastPage = keyCrmPage.lastPage() == null ? page : keyCrmPage.lastPage();
            page++;
            requestsSincePause++;

            if (requestsSincePause >= THROTTLE_BATCH_SIZE && page <= lastPage) {
                log.info("Full order sync: {} orders synced so far, {}/{} pages done; pausing {}s for KeyCRM's rate limit",
                        totalSynced, page - 1, lastPage, THROTTLE_PAUSE.toSeconds());
                sleep(THROTTLE_PAUSE);
                requestsSincePause = 0;
            }
        } while (page <= lastPage);

        log.info("Full order sync complete: {} orders across {} pages", totalSynced, page - 1);
        return totalSynced;
    }

    /**
     * Quick manual sync of the most recent {@code LATEST_ORDERS_PAGE_COUNT * MAX_PAGE_SIZE}
     * (500) orders ({@code sort=-id}, newest first) - e.g. a morning top-up between the last
     * nightly {@link #syncAllOrders()} run and now, without re-scanning the whole catalog.
     * Only {@value LATEST_ORDERS_PAGE_COUNT} requests, so no throttling needed.
     */
    public int syncLatestOrders() {
        int totalSynced = 0;

        for (int page = 1; page <= LATEST_ORDERS_PAGE_COUNT; page++) {
            KeyCrmPage<KeyCrmOrder> keyCrmPage = orderClient.getLatestOrdersSortedById(page, MAX_PAGE_SIZE);
            totalSynced += savePage(keyCrmPage.data());

            if (keyCrmPage.lastPage() != null && page >= keyCrmPage.lastPage()) {
                break; // fewer than 500 orders exist in total
            }
        }

        log.info("Latest-orders sync complete: {} orders synced", totalSynced);
        return totalSynced;
    }

    private int savePage(List<KeyCrmOrder> keyCrmOrders) {
        List<OrderEntity> entities = keyCrmOrders.stream().map(orderEntityMapper::toEntity).toList();
        orderRepository.saveAll(entities);
        return entities.size();
    }

    private static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while pausing for KeyCRM's rate limit", e);
        }
    }

}
