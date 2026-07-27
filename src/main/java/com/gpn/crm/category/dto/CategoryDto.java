package com.gpn.crm.category.dto;

public record CategoryDto(
        Long id,
        String name,
        Long parentId
) {
}
