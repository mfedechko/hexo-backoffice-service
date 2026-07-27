package com.gpn.crm.report.dto;

import java.util.List;

public record WarehouseStockReport(
        String warehouseName,
        List<WarehouseStockRowDto> rows
) {
}
