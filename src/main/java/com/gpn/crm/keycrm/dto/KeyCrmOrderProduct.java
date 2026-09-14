package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Raw shape of a line item under GET /order?include=products.offer (verified against a live
 * call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmOrderProduct(
        Long id,
        String sku,
        @JsonProperty("variation_id") Long variationId,
        @JsonProperty("publication_source_uuid") String publicationSourceUuid,
        String name,
        Boolean upsale,
        BigDecimal price,
        @JsonProperty("discount_amount") BigDecimal discountAmount,
        @JsonProperty("discount_percent") BigDecimal discountPercent,
        @JsonProperty("individual_discount") BigDecimal individualDiscount,
        @JsonProperty("loyalty_discount") BigDecimal loyaltyDiscount,
        @JsonProperty("total_discount") BigDecimal totalDiscount,
        @JsonProperty("purchased_price") BigDecimal purchasedPrice,
        @JsonProperty("price_sold") BigDecimal priceSold,
        BigDecimal quantity,
        @JsonProperty("unit_type") String unitType,
        @JsonProperty("stock_status") String stockStatus,
        /** {@code []} when unset, an object ({@code {"thumbnail": "url"}}) otherwise - kept untyped. */
        Object picture,
        String comment,
        List<KeyCrmOfferProperty> properties,
        @JsonProperty("product_status_id") Long productStatusId,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("shipment_type") String shipmentType,
        KeyCrmOrderProductOffer offer,
        KeyCrmOrderWarehouse warehouse
) {
}
