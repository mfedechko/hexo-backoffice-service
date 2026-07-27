package com.gpn.crm.product.dto;

import java.math.BigDecimal;

/**
 * page/limit are passed straight through to KeyCRM; minAvailable and nameContains are
 * applied on our side (KeyCRM's own /products filters only support product_id, category_id,
 * is_archived).
 */
public record ProductQuery(
        int page,
        int limit,
        BigDecimal minAvailable,
        String nameContains
) {
}
