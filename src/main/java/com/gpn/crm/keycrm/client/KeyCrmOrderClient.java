package com.gpn.crm.keycrm.client;

import com.gpn.crm.keycrm.dto.KeyCrmOrder;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class KeyCrmOrderClient {

    /** KeyCRM expects {@code filter[created_between]} as {@code from,to} with UTC {@code yyyy-MM-dd HH:mm:ss} timestamps. */
    private static final DateTimeFormatter KEYCRM_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

    /** Every relation the app currently reads off an order - see the KeyCrmOrder* DTOs. */
    private static final String INCLUDE = "buyer,products.offer,manager,tags,status,marketing,"
            + "payments,shipping.lastHistory,shipping.deliveryService,expenses,custom_fields,assigned";

    private final RestClient keyCrmRestClient;

    public KeyCrmPage<KeyCrmOrder> getOrders(Instant createdFrom, Instant createdTo, int page, int limit) {
        String createdBetween = KEYCRM_TIMESTAMP.format(createdFrom) + "," + KEYCRM_TIMESTAMP.format(createdTo);
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/order")
                        .queryParam("filter[created_between]", createdBetween)
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .queryParam("include", INCLUDE)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<KeyCrmOrder>>() {
                });
    }

    /**
     * Every order, unfiltered, oldest first ({@code sort=id} - verified against a live call:
     * KeyCRM defaults to newest-first otherwise). Used for the full-catalog sync, where paging
     * needs a stable order across many requests rather than a date window.
     */
    public KeyCrmPage<KeyCrmOrder> getAllOrdersSortedById(int page, int limit) {
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/order")
                        .queryParam("sort", "id")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .queryParam("include", INCLUDE)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<KeyCrmOrder>>() {
                });
    }

    /**
     * Every order, unfiltered, newest first ({@code sort=-id} - verified against a live call).
     * Used for the "latest orders" sync, where you want the most recent N orders regardless of
     * what date they fall on.
     */
    public KeyCrmPage<KeyCrmOrder> getLatestOrdersSortedById(int page, int limit) {
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/order")
                        .queryParam("sort", "-id")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .queryParam("include", INCLUDE)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<KeyCrmOrder>>() {
                });
    }
}
