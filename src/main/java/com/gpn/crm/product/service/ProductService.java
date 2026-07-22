package com.gpn.crm.product.service;

import com.gpn.crm.category.dto.CategoryDto;
import com.gpn.crm.category.service.CategoryService;
import com.gpn.crm.keycrm.client.KeyCrmOfferClient;
import com.gpn.crm.keycrm.client.KeyCrmProductClient;
import com.gpn.crm.keycrm.dto.KeyCrmOffer;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.keycrm.dto.KeyCrmProduct;
import com.gpn.crm.product.dto.OfferDto;
import com.gpn.crm.product.dto.ProductDto;
import com.gpn.crm.product.dto.ProductOffersDto;
import com.gpn.crm.product.dto.ProductPageDto;
import com.gpn.crm.product.dto.ProductQuery;
import com.gpn.crm.product.mapper.OfferMapper;
import com.gpn.crm.product.mapper.ProductMapper;
import com.gpn.crm.stock.dto.WarehouseStockDto;
import com.gpn.crm.stock.service.StockService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    /** KeyCRM's max page size, used to fetch every offer of a product in as few requests as possible. */
    private static final int MAX_PAGE_SIZE = 50;

    private final KeyCrmProductClient keyCrmProductClient;
    private final KeyCrmOfferClient keyCrmOfferClient;
    private final ProductMapper productMapper;
    private final OfferMapper offerMapper;
    private final StockService stockService;
    private final CategoryService categoryService;

    public ProductService(KeyCrmProductClient keyCrmProductClient,
                           KeyCrmOfferClient keyCrmOfferClient,
                           ProductMapper productMapper,
                           OfferMapper offerMapper,
                           StockService stockService,
                           CategoryService categoryService) {
        this.keyCrmProductClient = keyCrmProductClient;
        this.keyCrmOfferClient = keyCrmOfferClient;
        this.productMapper = productMapper;
        this.offerMapper = offerMapper;
        this.stockService = stockService;
        this.categoryService = categoryService;
    }

    public ProductPageDto getProducts(ProductQuery query) {
        KeyCrmPage<KeyCrmProduct> page = keyCrmProductClient.getProducts(query.page(), query.limit());
        Map<Long, CategoryDto> categoriesById = categoryService.getCategoriesById();

        List<ProductDto> items = page.data().stream()
                .map(product -> productMapper.toDto(product, categoriesById.get(product.categoryId())))
                .filter(dto -> matchesMinAvailable(dto, query.minAvailable()))
                .filter(dto -> matchesName(dto, query.nameContains()))
                .toList();

        return new ProductPageDto(
                page.currentPage() == null ? query.page() : page.currentPage(),
                page.perPage() == null ? query.limit() : page.perPage(),
                page.lastPage() == null ? 0 : page.lastPage(),
                page.total() == null ? items.size() : page.total(),
                items
        );
    }

    public ProductDto getProduct(long productId) {
        KeyCrmProduct product = keyCrmProductClient.getProduct(productId);
        CategoryDto category = categoryService.getCategoriesById().get(product.categoryId());
        return productMapper.toDto(product, category);
    }

    public ProductOffersDto getProductOffers(long productId) {
        KeyCrmProduct product = keyCrmProductClient.getProduct(productId);
        Map<Long, List<WarehouseStockDto>> warehousesByOfferId = stockService.getWarehouseBreakdownByOfferId();

        List<OfferDto> offers = fetchAllOffers(productId).stream()
                .map(offer -> offerMapper.toDto(offer, warehousesByOfferId.getOrDefault(offer.id(), List.of())))
                .toList();

        return new ProductOffersDto(product.id(), product.name(), offers);
    }

    private List<KeyCrmOffer> fetchAllOffers(long productId) {
        List<KeyCrmOffer> result = new ArrayList<>();

        int page = 1;
        int lastPage;
        do {
            KeyCrmPage<KeyCrmOffer> keyCrmPage = keyCrmOfferClient.getOffersByProductId(productId, page, MAX_PAGE_SIZE);
            result.addAll(keyCrmPage.data());

            lastPage = keyCrmPage.lastPage() == null ? page : keyCrmPage.lastPage();
            page++;
        } while (page <= lastPage);

        return result;
    }

    private boolean matchesMinAvailable(ProductDto dto, BigDecimal minAvailable) {
        return minAvailable == null || dto.available().compareTo(minAvailable) >= 0;
    }

    private boolean matchesName(ProductDto dto, String nameContains) {
        return nameContains == null
                || nameContains.isBlank()
                || (dto.name() != null && dto.name().toLowerCase().contains(nameContains.toLowerCase()));
    }
}
