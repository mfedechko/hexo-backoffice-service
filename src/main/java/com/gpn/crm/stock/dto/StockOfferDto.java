package com.gpn.crm.stock.dto;

import java.math.BigDecimal;
import java.util.List;

public record StockOfferDto(
        Long offerId,
        String sku,
        BigDecimal quantity,
        BigDecimal reserved,
        BigDecimal available,
        List<WarehouseStockDto> warehouses
) {
}
