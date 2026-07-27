package com.gpn.crm.report.dto;

import com.gpn.crm.product.dto.OfferPropertyDto;

import java.math.BigDecimal;
import java.util.List;

public record WarehouseStockRowDto(
        BigDecimal quantityInWarehouse,
        String categoryName,
        String sku,
        String productName,
        List<OfferPropertyDto> properties,
        String coating
) {
}
