package com.gpn.crm.order.service;

import com.gpn.crm.keycrm.client.KeyCrmOrderClient;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.order.dto.OrderDto;
import com.gpn.crm.order.dto.OrderQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    /** Incoming dates are calendar days in this zone; KeyCRM stores order timestamps in UTC. */
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("Europe/Kyiv");
    private static final int DEFAULT_WINDOW_DAYS = 30;
    /** KeyCRM's max page size, used to fetch every order in the range in as few requests as possible. */
    private static final int MAX_PAGE_SIZE = 50;

    private final KeyCrmOrderClient orderClient;

    /**
     * Fetches every order created in the given (inclusive) date range, paging through KeyCRM
     * internally since it caps each call at {@value MAX_PAGE_SIZE} items - same approach as
     * {@code WarehouseService}'s full-catalog scan.
     */
    public List<OrderDto> getOrders(OrderQuery query) {
        LocalDate today = LocalDate.now(BUSINESS_ZONE);
        LocalDate from = query.dateFrom();
        LocalDate to = query.dateTo();
        if (from == null) {
            from = (to != null ? to : today).minusDays(DEFAULT_WINDOW_DAYS);
        }
        if (to == null) {
            to = today;
        }

        // Kyiv wall-clock day bounds -> UTC instants (DST-aware). `to` is inclusive of the
        // whole day, so its upper bound is the start of the following day.
        Instant createdFrom = from.atStartOfDay(BUSINESS_ZONE).toInstant();
        Instant createdTo = to.plusDays(1).atStartOfDay(BUSINESS_ZONE).toInstant();

        List<OrderDto> orders = new ArrayList<>();
        int page = 1;
        int lastPage;
        do {
            KeyCrmPage<OrderDto> keyCrmPage = orderClient.getOrders(createdFrom, createdTo, page, MAX_PAGE_SIZE);
            orders.addAll(keyCrmPage.data());

            lastPage = keyCrmPage.lastPage() == null ? page : keyCrmPage.lastPage();
            page++;
        } while (page <= lastPage);

        return orders;
    }

}
