package com.gpn.crm.keycrm.client;

import com.gpn.crm.keycrm.dto.KeyCrmCategory;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KeyCrmCategoryClient {

    private final RestClient keyCrmRestClient;

    public KeyCrmCategoryClient(RestClient keyCrmRestClient) {
        this.keyCrmRestClient = keyCrmRestClient;
    }

    public KeyCrmPage<KeyCrmCategory> getCategories(int page, int limit) {
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/products/categories")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<KeyCrmCategory>>() {
                });
    }
}
