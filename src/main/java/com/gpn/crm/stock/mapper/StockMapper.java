package com.gpn.crm.stock.mapper;

import com.gpn.crm.keycrm.dto.KeyCrmStockItem;
import com.gpn.crm.keycrm.dto.KeyCrmWarehouseStock;
import com.gpn.crm.stock.dto.StockOfferDto;
import com.gpn.crm.stock.dto.WarehouseStockDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class StockMapper {

    public StockOfferDto toDto(KeyCrmStockItem item) {
        BigDecimal quantity = nvl(item.quantity());
        BigDecimal reserved = nvl(item.reserve());

        List<WarehouseStockDto> warehouses = item.warehouse() == null
                ? List.of()
                : item.warehouse().stream().map(this::toDto).toList();

        return new StockOfferDto(
                item.id(),
                item.sku(),
                quantity,
                reserved,
                quantity.subtract(reserved),
                warehouses
        );
    }

    private WarehouseStockDto toDto(KeyCrmWarehouseStock warehouse) {
        return new WarehouseStockDto(warehouse.id(), warehouse.name(), nvl(warehouse.quantity()), nvl(warehouse.reserve()));
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
