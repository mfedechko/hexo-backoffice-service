package com.gpn.crm.product.dto;

import com.gpn.crm.stock.dto.WarehouseStockDto;

import java.math.BigDecimal;
import java.util.List;

public record OfferDto(
        Long id,
        String sku,
        String barcode,
        BigDecimal price,
        BigDecimal quantity,
        BigDecimal reserved,
        BigDecimal available,
        List<OfferPropertyDto> properties,
        List<WarehouseStockDto> warehouses
) {
}
