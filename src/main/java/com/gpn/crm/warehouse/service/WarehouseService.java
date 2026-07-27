package com.gpn.crm.warehouse.service;

import com.gpn.crm.keycrm.client.KeyCrmStockClient;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.keycrm.dto.KeyCrmStockItem;
import com.gpn.crm.keycrm.dto.KeyCrmWarehouseStock;
import com.gpn.crm.warehouse.dto.WarehouseDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseService {

    /** KeyCRM's max page size, used to fetch every stock item in as few requests as possible. */
    private static final int MAX_PAGE_SIZE = 50;

    private final KeyCrmStockClient keyCrmStockClient;

    public WarehouseService(KeyCrmStockClient keyCrmStockClient) {
        this.keyCrmStockClient = keyCrmStockClient;
    }

    /**
     * KeyCRM has no endpoint that lists warehouses directly - the only place warehouse
     * id/name appears is nested inside each /offers/stocks item (with filter[details]=true),
     * so this scans the full stock catalog and dedupes what it finds there.
     */
    public List<WarehouseDto> getWarehouses() {
        Map<Long, String> nameById = new LinkedHashMap<>();

        int page = 1;
        int lastPage;
        do {
            KeyCrmPage<KeyCrmStockItem> keyCrmPage = keyCrmStockClient.getStocks(page, MAX_PAGE_SIZE, true);
            for (KeyCrmStockItem item : keyCrmPage.data()) {
                if (item.warehouse() == null) {
                    continue;
                }
                for (KeyCrmWarehouseStock warehouse : item.warehouse()) {
                    if (warehouse.id() != null) {
                        nameById.putIfAbsent(warehouse.id(), warehouse.name());
                    }
                }
            }

            lastPage = keyCrmPage.lastPage() == null ? page : keyCrmPage.lastPage();
            page++;
        } while (page <= lastPage);

        return nameById.entrySet().stream()
                .map(entry -> new WarehouseDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(WarehouseDto::id))
                .toList();
    }
}
