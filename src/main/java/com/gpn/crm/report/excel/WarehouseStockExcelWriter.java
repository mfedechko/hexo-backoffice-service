package com.gpn.crm.report.excel;

import com.gpn.crm.product.dto.OfferPropertyDto;
import com.gpn.crm.report.dto.WarehouseStockRowDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarehouseStockExcelWriter {

    private static final String[] HEADERS = {
            "Кількість на складі",
            "Категорія",
            "Артикул",
            "Назва товару",
            "Властивості товару",
            "Покриття"
    };

    private static final int[] COLUMN_WIDTHS = {18, 25, 18, 50, 45, 25};

    public byte[] write(List<WarehouseStockRowDto> rows) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Залишки");
            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, COLUMN_WIDTHS[i] * 256);
            }

            int rowIndex = 1;
            for (WarehouseStockRowDto row : rows) {
                Row excelRow = sheet.createRow(rowIndex++);
                excelRow.createCell(0).setCellValue(row.quantityInWarehouse().doubleValue());
                excelRow.createCell(1).setCellValue(nvl(row.categoryName()));
                excelRow.createCell(2).setCellValue(nvl(row.sku()));
                excelRow.createCell(3).setCellValue(nvl(row.productName()));
                excelRow.createCell(4).setCellValue(formatProperties(row.properties()));
                excelRow.createCell(5).setCellValue(nvl(row.coating()));
            }

            sheet.createFreezePane(0, 1);

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate warehouse stock Excel report", e);
        }
    }

    private String formatProperties(List<OfferPropertyDto> properties) {
        if (properties == null || properties.isEmpty()) {
            return "";
        }
        return properties.stream()
                .map(property -> property.name() + ": " + property.value())
                .collect(Collectors.joining("; "));
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
}
