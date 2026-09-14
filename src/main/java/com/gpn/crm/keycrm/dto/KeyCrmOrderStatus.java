package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape of an order's status, from GET /order?include=status (verified against a live
 * call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmOrderStatus(
        Long id,
        String name,
        String alias,
        @JsonProperty("is_active") Boolean isActive,
        @JsonProperty("group_id") Long groupId,
        @JsonProperty("is_closing_order") Boolean isClosingOrder,
        @JsonProperty("is_reserved") Boolean isReserved,
        @JsonProperty("expiration_period") Long expirationPeriod,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("deleted_at") String deletedAt
) {
}
