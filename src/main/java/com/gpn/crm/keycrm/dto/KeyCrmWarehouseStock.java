package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmWarehouseStock(
        Long id,
        String name,
        BigDecimal quantity,
        BigDecimal reserve
) {
}
