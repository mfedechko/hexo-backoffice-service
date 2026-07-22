package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape from GET /products/categories (verified against a live call). There is no
 * single-category route; a specific one is fetched via filter[category_id] on the list.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmCategory(
        Long id,
        String name,
        @JsonProperty("parent_id") Long parentId
) {
}
