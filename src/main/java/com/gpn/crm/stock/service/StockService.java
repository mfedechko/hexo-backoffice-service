package com.gpn.crm.stock.service;

import com.gpn.crm.keycrm.client.KeyCrmStockClient;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.keycrm.dto.KeyCrmStockItem;
import com.gpn.crm.stock.dto.StockAllQuery;
import com.gpn.crm.stock.dto.StockOfferDto;
import com.gpn.crm.stock.dto.StockPageDto;
import com.gpn.crm.stock.dto.StockQuery;
import com.gpn.crm.stock.dto.WarehouseStockDto;
import com.gpn.crm.stock.mapper.StockMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockService {

    /** KeyCRM's max page size, used to fetch "all" offers in as few requests as possible. */
    private static final int MAX_PAGE_SIZE = 50;

    private final KeyCrmStockClient keyCrmStockClient;
    private final StockMapper stockMapper;

    public StockService(KeyCrmStockClient keyCrmStockClient, StockMapper stockMapper) {
        this.keyCrmStockClient = keyCrmStockClient;
        this.stockMapper = stockMapper;
    }

    public StockPageDto getStocks(StockQuery query) {
        KeyCrmPage<KeyCrmStockItem> page = keyCrmStockClient.getStocks(query.page(), query.limit(), query.details());

        List<StockOfferDto> items = page.data().stream()
                .map(stockMapper::toDto)
                .filter(dto -> matchesMinAvailable(dto, query.minAvailable()))
                .filter(dto -> matchesSku(dto, query.skuContains()))
                .toList();

        return new StockPageDto(
                page.currentPage() == null ? query.page() : page.currentPage(),
                page.perPage() == null ? query.limit() : page.perPage(),
                page.lastPage() == null ? 0 : page.lastPage(),
                page.total() == null ? items.size() : page.total(),
                items
        );
    }

    public List<StockOfferDto> getAllStocks(StockAllQuery query) {
        return fetchAllStockItems(query.details()).stream()
                .map(stockMapper::toDto)
                .filter(dto -> matchesMinAvailable(dto, query.minAvailable()))
                .filter(dto -> matchesSku(dto, query.skuContains()))
                .toList();
    }

    /**
     * Per-warehouse breakdown keyed by offer id, built by scanning every /offers/stocks page -
     * that endpoint has no way to filter by offer or product id, so this is a full ~600-row scan.
     * Only call it where the warehouse detail is actually needed.
     */
    public Map<Long, List<WarehouseStockDto>> getWarehouseBreakdownByOfferId() {
        Map<Long, List<WarehouseStockDto>> result = new HashMap<>();
        for (KeyCrmStockItem item : fetchAllStockItems(true)) {
            result.put(item.id(), stockMapper.toDto(item).warehouses());
        }
        return result;
    }

    private List<KeyCrmStockItem> fetchAllStockItems(boolean details) {
        List<KeyCrmStockItem> result = new ArrayList<>();

        int page = 1;
        int lastPage;
        do {
            KeyCrmPage<KeyCrmStockItem> keyCrmPage = keyCrmStockClient.getStocks(page, MAX_PAGE_SIZE, details);
            result.addAll(keyCrmPage.data());

            lastPage = keyCrmPage.lastPage() == null ? page : keyCrmPage.lastPage();
            page++;
        } while (page <= lastPage);

        return result;
    }

    private boolean matchesMinAvailable(StockOfferDto dto, BigDecimal minAvailable) {
        return minAvailable == null || dto.available().compareTo(minAvailable) >= 0;
    }

    private boolean matchesSku(StockOfferDto dto, String skuContains) {
        return skuContains == null
                || skuContains.isBlank()
                || (dto.sku() != null && dto.sku().toLowerCase().contains(skuContains.toLowerCase()));
    }
}
