package com.gpn.crm.report.controller;

import com.gpn.crm.report.dto.WarehouseStockRowDto;
import com.gpn.crm.report.excel.WarehouseStockExcelWriter;
import com.gpn.crm.report.service.WarehouseStockReportService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports/warehouse-stock")
public class WarehouseStockReportController {

    private static final MediaType XLSX_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final WarehouseStockReportService warehouseStockReportService;
    private final WarehouseStockExcelWriter warehouseStockExcelWriter;

    public WarehouseStockReportController(WarehouseStockReportService warehouseStockReportService,
                                          WarehouseStockExcelWriter warehouseStockExcelWriter) {
        this.warehouseStockReportService = warehouseStockReportService;
        this.warehouseStockExcelWriter = warehouseStockExcelWriter;
    }

    @GetMapping
    public List<WarehouseStockRowDto> getReport(@RequestParam(defaultValue = "4") long warehouseId) {
        return warehouseStockReportService.getWarehouseStockReport(warehouseId);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(@RequestParam(defaultValue = "4") long warehouseId) {
        List<WarehouseStockRowDto> rows = warehouseStockReportService.getWarehouseStockReport(warehouseId);
        byte[] excelBytes = warehouseStockExcelWriter.write(rows);

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename("warehouse-%d-stock.xlsx".formatted(warehouseId))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(XLSX_MEDIA_TYPE)
                .body(excelBytes);
    }
}
