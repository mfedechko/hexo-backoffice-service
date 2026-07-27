package com.gpn.crm.keycrm.client;

import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.keycrm.dto.KeyCrmStockItem;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KeyCrmStockClient {

    private final RestClient keyCrmRestClient;

    public KeyCrmStockClient(RestClient keyCrmRestClient) {
        this.keyCrmRestClient = keyCrmRestClient;
    }

    public KeyCrmPage<KeyCrmStockItem> getStocks(int page, int limit, boolean details) {
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/offers/stocks")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .queryParam("filter[details]", details)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<KeyCrmStockItem>>() {
                });
    }
}
