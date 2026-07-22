package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Raw shape of a variant from GET /offers?filter[product_id]=... (verified against a live
 * call). This is the only KeyCRM resource that carries product_id - /offers/stocks items don't.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmOffer(
        Long id,
        @JsonProperty("product_id") Long productId,
        String sku,
        String barcode,
        BigDecimal price,
        BigDecimal quantity,
        @JsonProperty("in_reserve") BigDecimal inReserve,
        List<KeyCrmOfferProperty> properties,
        @JsonProperty("is_archived") Boolean isArchived
) {
}
