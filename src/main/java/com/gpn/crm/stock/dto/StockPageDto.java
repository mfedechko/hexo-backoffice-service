package com.gpn.crm.stock.dto;

import java.util.List;

public record StockPageDto(
        int currentPage,
        int perPage,
        int lastPage,
        int total,
        List<StockOfferDto> items
) {
}
