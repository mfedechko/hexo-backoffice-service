package com.gpn.crm.report.controller;

import com.gpn.crm.report.dto.WarehouseStockExcelExport;
import com.gpn.crm.report.dto.WarehouseStockRowDto;
import com.gpn.crm.report.service.WarehouseStockReportService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/reports/warehouse-stock")
public class WarehouseStockReportController {

    private static final MediaType XLSX_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final WarehouseStockReportService warehouseStockReportService;

    public WarehouseStockReportController(WarehouseStockReportService warehouseStockReportService) {
        this.warehouseStockReportService = warehouseStockReportService;
    }

    @GetMapping
    public List<WarehouseStockRowDto> getReport(@RequestParam(defaultValue = "4") long warehouseId) {
        return warehouseStockReportService.getWarehouseStockReport(warehouseId).rows();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(@RequestParam(defaultValue = "4") long warehouseId) {
        WarehouseStockExcelExport export = warehouseStockReportService.generateExcelReport(warehouseId);

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(export.filename(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(XLSX_MEDIA_TYPE)
                .body(export.content());
    }
}
