package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Raw shape of a single item from GET /offers/stocks (verified against a live call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmStockItem(
        Long id,
        String sku,
        BigDecimal price,
        BigDecimal quantity,
        @JsonProperty("purchased_price") BigDecimal purchasedPrice,
        BigDecimal reserve,
        List<KeyCrmWarehouseStock> warehouse
) {
}
