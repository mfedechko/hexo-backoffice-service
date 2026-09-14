package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape of an order's UTM/marketing attribution, from GET /order?include=marketing
 * (verified against a live call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmMarketing(
        Long id,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("utm_source") String utmSource,
        @JsonProperty("utm_medium") String utmMedium,
        @JsonProperty("utm_campaign") String utmCampaign,
        @JsonProperty("utm_term") String utmTerm,
        @JsonProperty("utm_content") String utmContent,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}
