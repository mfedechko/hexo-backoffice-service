package com.gpn.crm.product.mapper;

import com.gpn.crm.keycrm.dto.KeyCrmOffer;
import com.gpn.crm.keycrm.dto.KeyCrmOfferProperty;
import com.gpn.crm.product.dto.OfferDto;
import com.gpn.crm.product.dto.OfferPropertyDto;
import com.gpn.crm.stock.dto.WarehouseStockDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OfferMapper {

    public OfferDto toDto(KeyCrmOffer offer, List<WarehouseStockDto> warehouses) {
        BigDecimal quantity = nvl(offer.quantity());
        BigDecimal reserved = nvl(offer.inReserve());

        List<OfferPropertyDto> properties = offer.properties() == null
                ? List.of()
                : offer.properties().stream().map(this::toDto).toList();

        return new OfferDto(
                offer.id(),
                offer.sku(),
                offer.barcode(),
                nvl(offer.price()),
                quantity,
                reserved,
                quantity.subtract(reserved),
                properties,
                warehouses
        );
    }

    private OfferPropertyDto toDto(KeyCrmOfferProperty property) {
        return new OfferPropertyDto(property.name(), property.value());
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
