package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Raw shape of the offer nested under an order line item, from
 * GET /order?include=products.offer (verified against a live call). Distinct from
 * {@link KeyCrmOffer} (the GET /offers resource) - same underlying KeyCRM entity, but this
 * include exposes a different set of fields (e.g. dimensions, is_default/is_archived).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmOrderProductOffer(
        Long id,
        @JsonProperty("product_id") Long productId,
        String sku,
        String barcode,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        BigDecimal price,
        @JsonProperty("purchased_price") BigDecimal purchasedPrice,
        BigDecimal quantity,
        @JsonProperty("in_reserve") BigDecimal inReserve,
        BigDecimal weight,
        BigDecimal length,
        BigDecimal height,
        BigDecimal width,
        List<KeyCrmOfferProperty> properties,
        @JsonProperty("is_default") Boolean isDefault,
        @JsonProperty("is_archived") Boolean isArchived,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}
