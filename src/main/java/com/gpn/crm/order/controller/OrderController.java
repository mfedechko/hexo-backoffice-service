package com.gpn.crm.order.controller;

import com.gpn.crm.order.dto.OrderDto;
import com.gpn.crm.order.dto.OrderQuery;
import com.gpn.crm.order.dto.OrderSyncResponse;
import com.gpn.crm.order.service.OrderService;
import com.gpn.crm.order.service.OrderSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderSyncService orderSyncService;

    @GetMapping
    public List<OrderDto> getOrders(
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        return orderService.getOrders(new OrderQuery(dateFrom, dateTo));
    }

    /**
     * Fetches every order in the range from KeyCRM and upserts it into the local orders tables
     * (V5__add_orders_tables.sql) - see {@code OrderSyncService}.
     */
    @PostMapping("/sync")
    public OrderSyncResponse syncOrders(
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        int syncedCount = orderSyncService.syncOrders(new OrderQuery(dateFrom, dateTo));
        return new OrderSyncResponse(syncedCount);
    }

    /**
     * Manual trigger for the same full-catalog sync {@code OrderSyncScheduler} runs once a day
     * - useful for testing or an on-demand resync. Can take a while (pages through every order
     * KeyCRM has, throttled to stay under its rate limit), so the caller should expect a slow
     * response rather than a timeout being a failure.
     */
    @PostMapping("/sync-all")
    public OrderSyncResponse syncAllOrders() {
        return new OrderSyncResponse(orderSyncService.syncAllOrders());
    }

    /**
     * Quick manual top-up of the most recent ~500 orders - for e.g. pulling in whatever came
     * in since last night's {@code syncAllOrders} run, without waiting for a full rescan.
     */
    @PostMapping("/sync-latest")
    public OrderSyncResponse syncLatestOrders() {
        return new OrderSyncResponse(orderSyncService.syncLatestOrders());
    }

}
