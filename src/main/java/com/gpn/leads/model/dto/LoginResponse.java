package com.gpn.leads.model.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String tokenType = "Bearer";
    private final String accessToken;

    public LoginResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
