package com.gpn.crm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycrm")
public record KeyCrmProperties(String baseUrl, String apiToken) {
}
