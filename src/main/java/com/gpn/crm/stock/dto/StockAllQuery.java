package com.gpn.crm.stock.dto;

import java.math.BigDecimal;

/**
 * Filters for fetching every offer across all KeyCRM pages; unlike StockQuery there is
 * no page/limit since the service paginates internally until it has everything.
 */
public record StockAllQuery(
        boolean details,
        BigDecimal minAvailable,
        String skuContains
) {
}
