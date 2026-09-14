package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Raw shape of an order's buyer, nested under GET /order?include=buyer (verified against a
 * live call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmBuyer(
        Long id,
        @JsonProperty("company_id") Long companyId,
        @JsonProperty("full_name") String fullName,
        String birthday,
        String phone,
        String email,
        String note,
        String picture,
        String image,
        @JsonProperty("orders_sum") BigDecimal ordersSum,
        BigDecimal discount,
        String currency,
        @JsonProperty("orders_count") Integer ordersCount,
        @JsonProperty("has_duplicates") Integer hasDuplicates,
        @JsonProperty("manager_id") Long managerId,
        @JsonProperty("deleted_at") String deletedAt,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}
