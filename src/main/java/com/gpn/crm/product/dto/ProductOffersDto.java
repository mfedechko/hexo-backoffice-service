package com.gpn.crm.product.dto;

import java.util.List;

public record ProductOffersDto(
        Long productId,
        String productName,
        List<OfferDto> offers
) {
}
