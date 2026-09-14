package com.gpn.crm.order.mapper;

import com.gpn.crm.keycrm.dto.KeyCrmOrder;
import com.gpn.crm.keycrm.dto.KeyCrmOrderProduct;
import com.gpn.crm.keycrm.dto.KeyCrmOrderProductOffer;
import com.gpn.crm.order.dto.OrderDto;
import com.gpn.crm.order.dto.OrderProductDto;
import com.gpn.crm.order.dto.OrderProductOfferOffer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Maps the raw {@link KeyCrmOrder} (and nested KeyCrmOrder* DTOs) to this service's own,
 * currently much smaller, {@link OrderDto} - see the KeyCrmOrder* DTOs in keycrm.dto for every
 * field KeyCRM actually returns.
 */
@Component
public class OrderMapper {

    public OrderDto toDto(KeyCrmOrder order) {
        List<OrderProductDto> products = order.products() == null
                ? List.of()
                : order.products().stream().map(this::toDto).toList();

        return new OrderDto(order.id(), order.sourceUuid(), products);
    }

    private OrderProductDto toDto(KeyCrmOrderProduct product) {
        return new OrderProductDto(
                product.id(),
                product.sku(),
                product.name(),
                nvl(product.price()).doubleValue(),
                product.stockStatus(),
                nvl(product.quantity()).intValue(),
                toDto(product.offer())
        );
    }

    private OrderProductOfferOffer toDto(KeyCrmOrderProductOffer offer) {
        return offer == null ? null : new OrderProductOfferOffer(offer.id(), offer.productId());
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
