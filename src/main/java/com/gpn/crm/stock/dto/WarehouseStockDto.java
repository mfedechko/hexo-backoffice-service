package com.gpn.crm.stock.dto;

import java.math.BigDecimal;

public record WarehouseStockDto(
        Long id,
        String name,
        BigDecimal quantity,
        BigDecimal reserve
) {
}
