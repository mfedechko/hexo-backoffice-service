package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmRole(
        Long id,
        String name,
        String alias,
        String color
) {
}
