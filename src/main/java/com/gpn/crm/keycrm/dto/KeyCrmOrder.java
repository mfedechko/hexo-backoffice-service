package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Raw shape of an order from GET /order (verified against a live call with
 * include=buyer, products.offer, manager, tags, status, marketing, payments,
 * shipping.lastHistory, shipping.deliveryService, expenses, custom_fields, assigned).
 * <p>
 * Timestamp fields are kept as the raw KeyCRM strings rather than parsed here - their formats
 * aren't consistent across fields (most are ISO-8601 with a trailing Z and microseconds, but
 * e.g. last_synced_at has no "T"/"Z").
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmOrder(
        Long id,
        @JsonProperty("source_uuid") String sourceUuid,
        @JsonProperty("global_source_uuid") String globalSourceUuid,
        @JsonProperty("status_on_source") String statusOnSource,
        @JsonProperty("source_id") Long sourceId,
        @JsonProperty("client_id") Long clientId,
        @JsonProperty("grand_total") BigDecimal grandTotal,
        @JsonProperty("total_discount") BigDecimal totalDiscount,
        @JsonProperty("margin_sum") BigDecimal marginSum,
        @JsonProperty("expenses_sum") BigDecimal expensesSum,
        @JsonProperty("discount_amount") BigDecimal discountAmount,
        @JsonProperty("discount_percent") BigDecimal discountPercent,
        @JsonProperty("shipping_price") BigDecimal shippingPrice,
        /** Always null in every order observed so far; shape unverified. */
        Object taxes,
        @JsonProperty("register_id") Long registerId,
        /** Always [] in every order observed so far; element shape unverified. */
        @JsonProperty("fiscal_result") List<Object> fiscalResult,
        @JsonProperty("fiscal_status") String fiscalStatus,
        @JsonProperty("shipping_type_id") Long shippingTypeId,
        @JsonProperty("status_group_id") Long statusGroupId,
        @JsonProperty("status_id") Long statusId,
        @JsonProperty("closed_from") String closedFrom,
        @JsonProperty("status_expired_at") String statusExpiredAt,
        @JsonProperty("status_changed_at") String statusChangedAt,
        @JsonProperty("parent_id") Long parentId,
        @JsonProperty("manager_comment") String managerComment,
        @JsonProperty("client_comment") String clientComment,
        /**
         * {@code []} when unset, otherwise an object like
         * {@code {"individual": {"discount": 0, "client_id": 123, "auto_apply": true}}} - kept
         * untyped since the shape differs by value.
         */
        @JsonProperty("discount_data") Object discountData,
        @JsonProperty("is_gift") Boolean isGift,
        String promocode,
        @JsonProperty("wrap_price") BigDecimal wrapPrice,
        @JsonProperty("gift_wrap") Boolean giftWrap,
        @JsonProperty("payment_status") String paymentStatus,
        @JsonProperty("gift_message") String giftMessage,
        @JsonProperty("last_synced_at") String lastSyncedAt,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("closed_at") String closedAt,
        @JsonProperty("ordered_at") String orderedAt,
        @JsonProperty("source_updated_at") String sourceUpdatedAt,
        @JsonProperty("deleted_at") String deletedAt,
        @JsonProperty("payments_total") BigDecimal paymentsTotal,
        @JsonProperty("products_total") BigDecimal productsTotal,
        @JsonProperty("is_expired") Boolean isExpired,
        @JsonProperty("has_reserves") Boolean hasReserves,
        @JsonProperty("buyer_comment") String buyerComment,
        KeyCrmBuyer buyer,
        List<KeyCrmOrderProduct> products,
        KeyCrmManager manager,
        List<KeyCrmTag> tags,
        KeyCrmOrderStatus status,
        KeyCrmMarketing marketing,
        List<KeyCrmPayment> payments,
        KeyCrmShipping shipping,
        List<KeyCrmPayment> expenses,
        @JsonProperty("custom_fields") List<KeyCrmCustomField> customFields,
        List<KeyCrmAssignedUser> assigned,
        KeyCrmOrderWarehouse warehouse
) {
}
