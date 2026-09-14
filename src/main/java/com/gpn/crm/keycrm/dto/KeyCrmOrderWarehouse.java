package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape of the warehouse nested under an order (order-level and per-product), from
 * GET /order (verified against a live call). Distinct from {@link KeyCrmWarehouseStock} (the
 * GET /offers/stocks item) - same underlying KeyCRM entity, different fields exposed here.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmOrderWarehouse(
        Long id,
        String name,
        String description,
        @JsonProperty("is_active") Boolean isActive,
        @JsonProperty("is_default") Boolean isDefault,
        @JsonProperty("address_id") Long addressId,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("deleted_at") String deletedAt
) {
}
