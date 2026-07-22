package com.gpn.crm.keycrm.client;

import com.gpn.crm.keycrm.dto.KeyCrmOffer;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KeyCrmOfferClient {

    private final RestClient keyCrmRestClient;

    public KeyCrmOfferClient(RestClient keyCrmRestClient) {
        this.keyCrmRestClient = keyCrmRestClient;
    }

    public KeyCrmPage<KeyCrmOffer> getOffersByProductId(long productId, int page, int limit) {
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/offers")
                        .queryParam("filter[product_id]", productId)
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<KeyCrmOffer>>() {
                });
    }

    public KeyCrmPage<KeyCrmOffer> getOffers(int page, int limit) {
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/offers")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<KeyCrmOffer>>() {
                });
    }
}
