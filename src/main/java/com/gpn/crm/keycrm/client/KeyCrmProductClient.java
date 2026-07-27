package com.gpn.crm.keycrm.client;

import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.keycrm.dto.KeyCrmProduct;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KeyCrmProductClient {

    private final RestClient keyCrmRestClient;

    public KeyCrmProductClient(RestClient keyCrmRestClient) {
        this.keyCrmRestClient = keyCrmRestClient;
    }

    public KeyCrmPage<KeyCrmProduct> getProducts(int page, int limit) {
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/products")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<KeyCrmProduct>>() {
                });
    }

    public KeyCrmProduct getProduct(long productId) {
        return keyCrmRestClient.get()
                .uri("/products/{id}", productId)
                .retrieve()
                .body(KeyCrmProduct.class);
    }
}
