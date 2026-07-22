package com.gpn.crm.stock.dto;

import java.math.BigDecimal;

/**
 * page/limit/details are passed straight through to KeyCRM; minAvailable and skuContains
 * are applied on our side after the response comes back.
 */
public record StockQuery(
        int page,
        int limit,
        boolean details,
        BigDecimal minAvailable,
        String skuContains
) {
}
