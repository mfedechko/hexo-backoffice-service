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
import com.gpn.crm.report.dto.WarehouseStockRowDto;
import com.gpn.crm.stock.dto.WarehouseStockDto;
import com.gpn.crm.stock.service.StockService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarehouseStockReportService {

    /** KeyCRM's max page size, used to fetch every product/offer in as few requests as possible. */
    private static final int MAX_PAGE_SIZE = 50;

    private static final String COATING_PROPERTY_NAME = "Покриття";

    private final KeyCrmProductClient keyCrmProductClient;
    private final KeyCrmOfferClient keyCrmOfferClient;
    private final StockService stockService;
    private final CategoryService categoryService;

    public WarehouseStockReportService(KeyCrmProductClient keyCrmProductClient,
                                       KeyCrmOfferClient keyCrmOfferClient,
                                       StockService stockService,
                                       CategoryService categoryService) {
        this.keyCrmProductClient = keyCrmProductClient;
        this.keyCrmOfferClient = keyCrmOfferClient;
        this.stockService = stockService;
        this.categoryService = categoryService;
    }

    /**
     * Builds one row per offer that has stock at the given warehouse. KeyCRM has no single
     * endpoint for this, so it joins three full-catalog scans: /products (names), /offers
     * (sku + properties + product_id) and /offers/stocks (per-warehouse quantity) - roughly
     * 30 upstream requests for the current catalog size.
     */
    public List<WarehouseStockRowDto> getWarehouseStockReport(long warehouseId) {
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
                    .orElse(null);

            if (quantityInWarehouse == null || quantityInWarehouse.signum() <= 0) {
                continue;
            }

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

        return rows;
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
