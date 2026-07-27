package com.gpn.crm.product.dto;

import com.gpn.crm.category.dto.CategoryDto;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        String thumbnailUrl,
        BigDecimal quantity,
        BigDecimal reserved,
        BigDecimal available,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String currencyCode,
        boolean hasOffers,
        boolean archived,
        CategoryDto category
) {
}
