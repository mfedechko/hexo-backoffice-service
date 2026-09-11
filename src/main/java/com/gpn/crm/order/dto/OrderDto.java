package com.gpn.crm.order.dto;

import java.util.List;

public record OrderDto(
        Long id,
        String sourceUuid,
        List<OrderProductDto> products
) {
}
