package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Raw shape of an order custom field, from GET /order?include=custom_fields (verified against
 * a live call). {@code value} is kept untyped - every field observed so far is {@code type:
 * "select"} with a {@code List<String>} value, but other custom-field types are known to use a
 * plain scalar value instead.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmCustomField(
        Long id,
        String uuid,
        String name,
        String type,
        Object value
) {
}
