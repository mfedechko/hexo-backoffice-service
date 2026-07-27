package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * KeyCRM's paginated list envelope (Laravel-style pagination), generic over the item type.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmPage<T>(
        Integer total,
        @JsonProperty("current_page") Integer currentPage,
        @JsonProperty("per_page") Integer perPage,
        @JsonProperty("last_page") Integer lastPage,
        List<T> data
) {
}
