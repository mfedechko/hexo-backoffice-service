package com.gpn.crm.keycrm.client;

import com.gpn.crm.keycrm.dto.KeyCrmPage;
import com.gpn.crm.order.dto.OrderDto;
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

    private final RestClient keyCrmRestClient;

    public KeyCrmPage<OrderDto> getOrders(Instant createdFrom, Instant createdTo, int page, int limit) {
        String createdBetween = KEYCRM_TIMESTAMP.format(createdFrom) + "," + KEYCRM_TIMESTAMP.format(createdTo);
        return keyCrmRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/order")
                        .queryParam("filter[created_between]", createdBetween)
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .queryParam("include", "products.offer")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<KeyCrmPage<OrderDto>>() {
                });
    }
}
