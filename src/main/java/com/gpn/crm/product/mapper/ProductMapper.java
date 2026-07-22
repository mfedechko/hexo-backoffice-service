package com.gpn.crm.product.mapper;

import com.gpn.crm.category.dto.CategoryDto;
import com.gpn.crm.keycrm.dto.KeyCrmProduct;
import com.gpn.crm.product.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {

    public ProductDto toDto(KeyCrmProduct product, CategoryDto category) {
        BigDecimal quantity = nvl(product.quantity());
        BigDecimal reserved = nvl(product.inReserve());

        return new ProductDto(
                product.id(),
                product.name(),
                product.thumbnailUrl(),
                quantity,
                reserved,
                quantity.subtract(reserved),
                nvl(product.minPrice()),
                nvl(product.maxPrice()),
                product.currencyCode(),
                Boolean.TRUE.equals(product.hasOffers()),
                Boolean.TRUE.equals(product.isArchived()),
                category
        );
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
