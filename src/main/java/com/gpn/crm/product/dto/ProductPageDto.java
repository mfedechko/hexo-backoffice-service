package com.gpn.crm.product.dto;

import java.util.List;

public record ProductPageDto(
        int currentPage,
        int perPage,
        int lastPage,
        int total,
        List<ProductDto> items
) {
}
