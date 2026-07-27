package com.gpn.crm.report.controller;

import com.gpn.crm.report.dto.WarehouseStockReport;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/reports/warehouse-stock")
public class WarehouseStockReportController {

    private static final MediaType XLSX_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    /** Characters that aren't safe across filesystems/browsers in a download filename. */
    private static final String UNSAFE_FILENAME_CHARS = "[\\\\/:*?\"<>|]";

    private final WarehouseStockReportService warehouseStockReportService;
    private final WarehouseStockExcelWriter warehouseStockExcelWriter;

    public WarehouseStockReportController(WarehouseStockReportService warehouseStockReportService,
                                          WarehouseStockExcelWriter warehouseStockExcelWriter) {
        this.warehouseStockReportService = warehouseStockReportService;
        this.warehouseStockExcelWriter = warehouseStockExcelWriter;
    }

    @GetMapping
    public List<WarehouseStockRowDto> getReport(@RequestParam(defaultValue = "4") long warehouseId) {
        return warehouseStockReportService.getWarehouseStockReport(warehouseId).rows();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(@RequestParam(defaultValue = "4") long warehouseId) {
        WarehouseStockReport report = warehouseStockReportService.getWarehouseStockReport(warehouseId);
        byte[] excelBytes = warehouseStockExcelWriter.write(report.rows());

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(filenameFor(report.warehouseName()), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(XLSX_MEDIA_TYPE)
                .body(excelBytes);
    }

    private String filenameFor(String warehouseName) {
        String sanitized = warehouseName.replaceAll(UNSAFE_FILENAME_CHARS, "").trim();
        return "%s.xlsx".formatted(sanitized.isEmpty() ? warehouseName : sanitized);
    }
}
