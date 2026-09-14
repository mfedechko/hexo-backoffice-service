package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape of an order tag, from GET /order?include=tags (verified against a live call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmTag(
        Long id,
        String name,
        String alias,
        String color,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}
