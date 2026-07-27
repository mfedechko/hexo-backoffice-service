package com.gpn.crm.report.dto;

public record WarehouseStockExcelExport(
        String filename,
        byte[] content
) {
}
