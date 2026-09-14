package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Raw shape of an order's shipping info, from
 * GET /order?include=shipping.lastHistory, shipping.deliveryService (verified against a live
 * call). Note there's no {@code id} field on this object in KeyCRM's response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmShipping(
        @JsonProperty("delivery_service_id") Long deliveryServiceId,
        @JsonProperty("address_id") Long addressId,
        @JsonProperty("last_history_id") Long lastHistoryId,
        @JsonProperty("tracking_code") String trackingCode,
        @JsonProperty("return_tracking_code") String returnTrackingCode,
        @JsonProperty("tracking_code_send_at") String trackingCodeSendAt,
        @JsonProperty("shipping_status") String shippingStatus,
        /** {@code []} when empty, an object (uuid/package_type/document_templates) otherwise - kept untyped. */
        @JsonProperty("shipment_payload") Object shipmentPayload,
        /** {@code []} when empty, an object (city_ref/city_desc/...) otherwise - kept untyped. */
        @JsonProperty("address_payload") Object addressPayload,
        @JsonProperty("is_warehouse") Boolean isWarehouse,
        @JsonProperty("shipping_preferred_method") String shippingPreferredMethod,
        @JsonProperty("shipping_address") String shippingAddress,
        @JsonProperty("recipient_phone") String recipientPhone,
        @JsonProperty("recipient_full_name") String recipientFullName,
        @JsonProperty("shipping_address_country") String shippingAddressCountry,
        @JsonProperty("shipping_address_country_code") String shippingAddressCountryCode,
        @JsonProperty("shipping_address_region") String shippingAddressRegion,
        @JsonProperty("shipping_address_city") String shippingAddressCity,
        @JsonProperty("shipping_address_zip") String shippingAddressZip,
        @JsonProperty("shipping_receive_point") String shippingReceivePoint,
        @JsonProperty("shipping_secondary_line") String shippingSecondaryLine,
        @JsonProperty("shipping_date") String shippingDate,
        @JsonProperty("shipping_date_actual") String shippingDateActual,
        @JsonProperty("shipping_date_actual_has_owner") Boolean shippingDateActualHasOwner,
        @JsonProperty("shipping_price") BigDecimal shippingPrice,
        @JsonProperty("was_shipped") Boolean wasShipped,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("deleted_at") String deletedAt,
        @JsonProperty("full_address") String fullAddress,
        @JsonProperty("last_history") KeyCrmShippingHistory lastHistory,
        @JsonProperty("delivery_service") KeyCrmDeliveryService deliveryService
) {
}
