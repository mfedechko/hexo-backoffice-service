package com.gpn.crm.order.dto;

import java.time.LocalDate;

/**
 * dateFrom/dateTo are optional, inclusive calendar-day bounds interpreted in Europe/Kyiv time;
 * {@code OrderService} fills in a default 30-day window when they're missing and converts them
 * to a UTC {@code filter[created_between]} range before calling KeyCRM. There's no page/limit
 * here - {@code OrderService} fetches every order in the range by paging through KeyCRM
 * internally (KeyCRM caps each call at 50 items).
 */
public record OrderQuery(
        LocalDate dateFrom,
        LocalDate dateTo
) {
}
