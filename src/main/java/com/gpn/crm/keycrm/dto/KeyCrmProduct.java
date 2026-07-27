package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Raw shape of a product from GET /products and GET /products/{id} (verified against a live call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmProduct(
        Long id,
        String name,
        String description,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        @JsonProperty("attachments_data") List<String> attachmentsData,
        BigDecimal quantity,
        @JsonProperty("in_reserve") BigDecimal inReserve,
        @JsonProperty("currency_code") String currencyCode,
        @JsonProperty("min_price") BigDecimal minPrice,
        @JsonProperty("max_price") BigDecimal maxPrice,
        @JsonProperty("has_offers") Boolean hasOffers,
        @JsonProperty("is_archived") Boolean isArchived,
        @JsonProperty("category_id") Long categoryId
) {
}
