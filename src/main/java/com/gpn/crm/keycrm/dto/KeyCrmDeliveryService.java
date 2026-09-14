package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape of a shipment's carrier, from GET /order?include=shipping.deliveryService
 * (verified against a live call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmDeliveryService(
        Long id,
        String name,
        @JsonProperty("source_name") String sourceName,
        String alias
) {
}
