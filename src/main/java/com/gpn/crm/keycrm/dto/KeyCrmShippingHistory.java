package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape of a shipment's latest tracking event, from
 * GET /order?include=shipping.lastHistory (verified against a live call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmShippingHistory(
        Long id,
        @JsonProperty("tracking_code") String trackingCode,
        @JsonProperty("last_office_index") Long lastOfficeIndex,
        @JsonProperty("status_code") String statusCode,
        String description,
        @JsonProperty("shipping_status") String shippingStatus,
        @JsonProperty("shipping_date") String shippingDate,
        String name,
        String index,
        String country,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}
