package com.gpn.crm.order.dto;

public record OrderProductDto(
        Long id,
        String sku,
        String name,
        double price,
        String stockStatus,
        int quantity,
        OrderProductOfferOffer offer
) {
}
