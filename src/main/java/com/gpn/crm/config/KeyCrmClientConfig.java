package com.gpn.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class KeyCrmClientConfig {

    @Bean
    public RestClient keyCrmRestClient(RestClient.Builder builder, KeyCrmProperties properties) {
        // Spring's @ConfigurationProperties binding leaves an unresolvable ${VAR} as literal
        // text instead of failing the bind, so an unset KEYCRM_API_TOKEN would otherwise go
        // unnoticed until KeyCRM rejects the request with a 401.
        String apiToken = properties.apiToken();
        if (apiToken == null || apiToken.isBlank() || apiToken.contains("${")) {
            throw new IllegalStateException(
                    "keycrm.api-token is not set. Export KEYCRM_API_TOKEN in the environment the "
                            + "app actually runs in - a .env file is not read automatically by Spring Boot.");
        }

        return builder
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
