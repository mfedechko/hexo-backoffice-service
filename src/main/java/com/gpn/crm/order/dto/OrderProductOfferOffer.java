package com.gpn.crm.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderProductOfferOffer(
        Long id,
        @JsonProperty("product_id")
        Long productId
) {
}
