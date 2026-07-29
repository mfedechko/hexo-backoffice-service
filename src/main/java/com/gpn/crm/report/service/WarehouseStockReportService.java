package com.gpn.crm.report.service;

import com.gpn.crm.category.dto.CategoryDto;
import com.gpn.crm.category.service.CategoryService;
import com.gpn.crm.keycrm.client.KeyCrmOfferClient;
import com.gpn.crm.keycrm.client.KeyCrmProductClient;
import com.gpn.crm.keycrm.dto.KeyCrmOffer;
import com.gpn.crm.keycrm.dto.KeyCrmOfferProperty;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.keycrm.dto.KeyCrmProduct;
import com.gpn.crm.product.dto.OfferPropertyDto;
import com.gpn.crm.report.dto.WarehouseStockExcelExport;
import com.gpn.crm.report.dto.WarehouseStockReport;
import com.gpn.crm.report.dto.WarehouseStockRowDto;
import com.gpn.crm.report.excel.WarehouseStockExcelWriter;
import com.gpn.crm.stock.dto.WarehouseStockDto;
import com.gpn.crm.stock.service.StockService;
import com.gpn.loghistory.service.LogHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseStockReportService {

    /** KeyCRM's max page size, used to fetch every product/offer in as few requests as possible. */
    private static final int MAX_PAGE_SIZE = 50;

    private static final String COATING_PROPERTY_NAME = "Покриття";

    /** Characters that aren't safe across filesystems/browsers in a download filename. */
    private static final String UNSAFE_FILENAME_CHARS = "[\\\\/:*?\"<>|]";

    private final KeyCrmProductClient keyCrmProductClient;
    private final KeyCrmOfferClient keyCrmOfferClient;
    private final StockService stockService;
    private final CategoryService categoryService;
    private final WarehouseStockExcelWriter warehouseStockExcelWriter;
    private final LogHistoryService logHistoryService;

    public WarehouseStockExcelExport generateExcelReport(long warehouseId) {
        WarehouseStockReport report = getWarehouseStockReport(warehouseId);
        byte[] content = warehouseStockExcelWriter.write(report.rows());
        logHistoryService.logReportGeneration(warehouseId, report.warehouseName());
        return new WarehouseStockExcelExport(filenameFor(report.warehouseName()), content);
    }

    private String filenameFor(String warehouseName) {
        String sanitized = warehouseName.replaceAll(UNSAFE_FILENAME_CHARS, "").trim();
        return "%s.xlsx".formatted(sanitized.isEmpty() ? warehouseName : sanitized);
    }

    /**
     * Builds one row per offer at the given warehouse, including offers with zero (or no
     * recorded) stock there. KeyCRM has no single endpoint for this, so it joins three
     * full-catalog scans: /products (names), /offers (sku + properties + product_id) and
     * /offers/stocks (per-warehouse quantity) - roughly 30 upstream requests for the current
     * catalog size.
     */
    public WarehouseStockReport getWarehouseStockReport(long warehouseId) {
        List<KeyCrmProduct> products = fetchAllProducts();

        Map<Long, String> productNameById = products.stream()
                .collect(Collectors.toMap(KeyCrmProduct::id, KeyCrmProduct::name));

        Map<Long, CategoryDto> categoriesById = categoryService.getCategoriesById();
        Map<Long, String> categoryNameByProductId = products.stream()
                .filter(product -> categoriesById.containsKey(product.categoryId()))
                .collect(Collectors.toMap(KeyCrmProduct::id, product -> categoriesById.get(product.categoryId()).name()));

        Map<Long, List<WarehouseStockDto>> warehousesByOfferId = stockService.getWarehouseBreakdownByOfferId();

        List<WarehouseStockRowDto> rows = new ArrayList<>();
        for (KeyCrmOffer offer : fetchAllOffers()) {
            BigDecimal quantityInWarehouse = warehousesByOfferId.getOrDefault(offer.id(), List.of()).stream()
                    .filter(warehouse -> warehouse.id() != null && warehouse.id() == warehouseId)
                    .map(WarehouseStockDto::quantity)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);

            List<KeyCrmOfferProperty> properties = offer.properties() == null ? List.of() : offer.properties();

            List<OfferPropertyDto> otherProperties = properties.stream()
                    .filter(property -> !COATING_PROPERTY_NAME.equalsIgnoreCase(property.name()))
                    .map(property -> new OfferPropertyDto(property.name(), property.value()))
                    .toList();

            String coating = properties.stream()
                    .filter(property -> COATING_PROPERTY_NAME.equalsIgnoreCase(property.name()))
                    .map(KeyCrmOfferProperty::value)
                    .findFirst()
                    .orElse(null);

            rows.add(new WarehouseStockRowDto(
                    quantityInWarehouse,
                    categoryNameByProductId.get(offer.productId()),
                    offer.sku(),
                    productNameById.get(offer.productId()),
                    otherProperties,
                    coating
            ));
        }

        rows.sort(Comparator.comparing(WarehouseStockRowDto::quantityInWarehouse).reversed());

        String warehouseName = warehousesByOfferId.values().stream()
                .flatMap(List::stream)
                .filter(warehouse -> warehouse.id() != null && warehouse.id() == warehouseId && warehouse.name() != null)
                .map(WarehouseStockDto::name)
                .findFirst()
                .orElse("warehouse-" + warehouseId);

        return new WarehouseStockReport(warehouseName, rows);
    }

    private List<KeyCrmProduct> fetchAllProducts() {
        List<KeyCrmProduct> result = new ArrayList<>();

        int page = 1;
        int lastPage;
        do {
            KeyCrmPage<KeyCrmProduct> keyCrmPage = keyCrmProductClient.getProducts(page, MAX_PAGE_SIZE);
            result.addAll(keyCrmPage.data());

            lastPage = keyCrmPage.lastPage() == null ? page : keyCrmPage.lastPage();
            page++;
        } while (page <= lastPage);

        return result;
    }

    private List<KeyCrmOffer> fetchAllOffers() {
        List<KeyCrmOffer> result = new ArrayList<>();

        int page = 1;
        int lastPage;
        do {
            KeyCrmPage<KeyCrmOffer> keyCrmPage = keyCrmOfferClient.getOffers(page, MAX_PAGE_SIZE);
            result.addAll(keyCrmPage.data());

            lastPage = keyCrmPage.lastPage() == null ? page : keyCrmPage.lastPage();
            page++;
        } while (page <= lastPage);

        return result;
    }
}
