package com.gpn.users.model.dto;


public record UserDetailsDto(
        Long id,
        String username,
        String email
) {
}
