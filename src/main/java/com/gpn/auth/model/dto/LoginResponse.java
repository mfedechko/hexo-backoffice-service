package com.gpn.auth.model.dto;

import java.util.List;

public record LoginResponse(
        String tokenType,
        String accessToken,
        String username,
        String email,
        List<String> roles) {

    public LoginResponse(final String accessToken, final String username, final String email, final List<String> roles) {
        this("Bearer", accessToken, username, email, roles);
    }
}
