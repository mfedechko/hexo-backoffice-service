package com.gpn.crm.order.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * One row per KeyCRM order (V5__add_orders_tables.sql). {@code id} is KeyCRM's own order id,
 * not generated here. Single-object KeyCRM relations (buyer, manager, status, marketing,
 * shipping incl. last_history/delivery_service, warehouse) are flattened into prefixed columns
 * on this entity; list relations are child entities below, cascaded from here.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    private Long id;

    @Column(name = "source_uuid")
    private String sourceUuid;
    @Column(name = "global_source_uuid")
    private String globalSourceUuid;
    @Column(name = "status_on_source")
    private String statusOnSource;
    @Column(name = "source_id")
    private Long sourceId;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "grand_total")
    private BigDecimal grandTotal;
    @Column(name = "total_discount")
    private BigDecimal totalDiscount;
    @Column(name = "margin_sum")
    private BigDecimal marginSum;
    @Column(name = "expenses_sum")
    private BigDecimal expensesSum;
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;
    @Column(name = "discount_percent")
    private BigDecimal discountPercent;
    @Column(name = "shipping_price")
    private BigDecimal shippingPrice;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object taxes;
    @Column(name = "register_id")
    private Long registerId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "fiscal_result", columnDefinition = "jsonb")
    private Object fiscalResult;
    @Column(name = "fiscal_status")
    private String fiscalStatus;
    @Column(name = "shipping_type_id")
    private Long shippingTypeId;
    @Column(name = "status_group_id")
    private Long statusGroupId;
    @Column(name = "status_id")
    private Long statusId;
    @Column(name = "closed_from")
    private String closedFrom;
    @Column(name = "status_expired_at")
    private LocalDateTime statusExpiredAt;
    @Column(name = "status_changed_at")
    private LocalDateTime statusChangedAt;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "manager_comment")
    private String managerComment;
    @Column(name = "client_comment")
    private String clientComment;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "discount_data", columnDefinition = "jsonb")
    private Object discountData;
    @Column(name = "is_gift")
    private Boolean isGift;
    private String promocode;
    @Column(name = "wrap_price")
    private BigDecimal wrapPrice;
    @Column(name = "gift_wrap")
    private Boolean giftWrap;
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "gift_message")
    private String giftMessage;
    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;
    @Column(name = "source_updated_at")
    private LocalDateTime sourceUpdatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "payments_total")
    private BigDecimal paymentsTotal;
    @Column(name = "products_total")
    private BigDecimal productsTotal;
    @Column(name = "is_expired")
    private Boolean isExpired;
    @Column(name = "has_reserves")
    private Boolean hasReserves;
    @Column(name = "buyer_comment")
    private String buyerComment;

    // buyer
    @Column(name = "buyer_id")
    private Long buyerId;
    @Column(name = "buyer_company_id")
    private Long buyerCompanyId;
    @Column(name = "buyer_full_name")
    private String buyerFullName;
    @Column(name = "buyer_birthday")
    private LocalDateTime buyerBirthday;
    @Column(name = "buyer_phone")
    private String buyerPhone;
    @Column(name = "buyer_email")
    private String buyerEmail;
    @Column(name = "buyer_note")
    private String buyerNote;
    @Column(name = "buyer_picture")
    private String buyerPicture;
    @Column(name = "buyer_image")
    private String buyerImage;
    @Column(name = "buyer_orders_sum")
    private BigDecimal buyerOrdersSum;
    @Column(name = "buyer_discount")
    private BigDecimal buyerDiscount;
    @Column(name = "buyer_currency")
    private String buyerCurrency;
    @Column(name = "buyer_orders_count")
    private Integer buyerOrdersCount;
    @Column(name = "buyer_has_duplicates")
    private Integer buyerHasDuplicates;
    @Column(name = "buyer_manager_id")
    private Long buyerManagerId;
    @Column(name = "buyer_deleted_at")
    private LocalDateTime buyerDeletedAt;
    @Column(name = "buyer_created_at")
    private LocalDateTime buyerCreatedAt;
    @Column(name = "buyer_updated_at")
    private LocalDateTime buyerUpdatedAt;

    // manager
    @Column(name = "manager_id")
    private Long managerId;
    @Column(name = "manager_first_name")
    private String managerFirstName;
    @Column(name = "manager_last_name")
    private String managerLastName;
    @Column(name = "manager_email")
    private String managerEmail;
    @Column(name = "manager_username")
    private String managerUsername;
    @Column(name = "manager_phone")
    private String managerPhone;
    @Column(name = "manager_role_id")
    private Long managerRoleId;
    @Column(name = "manager_avatar_id")
    private Long managerAvatarId;
    @Column(name = "manager_status")
    private String managerStatus;
    @Column(name = "manager_last_logged_at")
    private LocalDateTime managerLastLoggedAt;
    @Column(name = "manager_ready_at")
    private LocalDateTime managerReadyAt;
    @Column(name = "manager_is_ready")
    private Boolean managerIsReady;
    @Column(name = "manager_use_2fa")
    private Boolean managerUse2fa;
    @Column(name = "manager_created_at")
    private LocalDateTime managerCreatedAt;
    @Column(name = "manager_updated_at")
    private LocalDateTime managerUpdatedAt;
    @Column(name = "manager_deleted_at")
    private LocalDateTime managerDeletedAt;
    @Column(name = "manager_full_name")
    private String managerFullName;

    // status catalog entry (id/group_id already covered by statusId/statusGroupId above)
    @Column(name = "status_name")
    private String statusName;
    @Column(name = "status_alias")
    private String statusAlias;
    @Column(name = "status_is_active")
    private Boolean statusIsActive;
    @Column(name = "status_is_closing_order")
    private Boolean statusIsClosingOrder;
    @Column(name = "status_is_reserved")
    private Boolean statusIsReserved;
    @Column(name = "status_expiration_period")
    private Long statusExpirationPeriod;
    @Column(name = "status_created_at")
    private LocalDateTime statusCreatedAt;
    @Column(name = "status_updated_at")
    private LocalDateTime statusUpdatedAt;
    @Column(name = "status_deleted_at")
    private LocalDateTime statusDeletedAt;

    // marketing / UTM attribution
    @Column(name = "marketing_id")
    private Long marketingId;
    @Column(name = "utm_source")
    private String utmSource;
    @Column(name = "utm_medium")
    private String utmMedium;
    @Column(name = "utm_campaign")
    private String utmCampaign;
    @Column(name = "utm_term")
    private String utmTerm;
    @Column(name = "utm_content")
    private String utmContent;
    @Column(name = "marketing_created_at")
    private LocalDateTime marketingCreatedAt;
    @Column(name = "marketing_updated_at")
    private LocalDateTime marketingUpdatedAt;

    // shipping (shippingPrice already covered by the order-level field above)
    @Column(name = "shipping_delivery_service_id")
    private Long shippingDeliveryServiceId;
    @Column(name = "shipping_address_id")
    private Long shippingAddressId;
    @Column(name = "shipping_last_history_id")
    private Long shippingLastHistoryId;
    @Column(name = "shipping_tracking_code")
    private String shippingTrackingCode;
    @Column(name = "shipping_return_tracking_code")
    private String shippingReturnTrackingCode;
    @Column(name = "shipping_tracking_code_send_at")
    private LocalDateTime shippingTrackingCodeSendAt;
    @Column(name = "shipping_status")
    private String shippingStatus;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "shipping_shipment_payload", columnDefinition = "jsonb")
    private Object shippingShipmentPayload;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "shipping_address_payload", columnDefinition = "jsonb")
    private Object shippingAddressPayload;
    @Column(name = "shipping_is_warehouse")
    private Boolean shippingIsWarehouse;
    @Column(name = "shipping_preferred_method")
    private String shippingPreferredMethod;
    @Column(name = "shipping_address")
    private String shippingAddress;
    @Column(name = "shipping_recipient_phone")
    private String shippingRecipientPhone;
    @Column(name = "shipping_recipient_full_name")
    private String shippingRecipientFullName;
    @Column(name = "shipping_address_country")
    private String shippingAddressCountry;
    @Column(name = "shipping_address_country_code")
    private String shippingAddressCountryCode;
    @Column(name = "shipping_address_region")
    private String shippingAddressRegion;
    @Column(name = "shipping_address_city")
    private String shippingAddressCity;
    @Column(name = "shipping_address_zip")
    private String shippingAddressZip;
    @Column(name = "shipping_receive_point")
    private String shippingReceivePoint;
    @Column(name = "shipping_secondary_line")
    private String shippingSecondaryLine;
    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;
    @Column(name = "shipping_date_actual")
    private LocalDateTime shippingDateActual;
    @Column(name = "shipping_date_actual_has_owner")
    private Boolean shippingDateActualHasOwner;
    @Column(name = "shipping_was_shipped")
    private Boolean shippingWasShipped;
    @Column(name = "shipping_created_at")
    private LocalDateTime shippingCreatedAt;
    @Column(name = "shipping_updated_at")
    private LocalDateTime shippingUpdatedAt;
    @Column(name = "shipping_deleted_at")
    private LocalDateTime shippingDeletedAt;
    @Column(name = "shipping_full_address")
    private String shippingFullAddress;
    // shipping.last_history (id covered by shippingLastHistoryId above)
    @Column(name = "shipping_history_last_office_index")
    private Long shippingHistoryLastOfficeIndex;
    @Column(name = "shipping_history_status_code")
    private String shippingHistoryStatusCode;
    @Column(name = "shipping_history_description")
    private String shippingHistoryDescription;
    @Column(name = "shipping_history_status")
    private String shippingHistoryStatus;
    @Column(name = "shipping_history_date")
    private LocalDateTime shippingHistoryDate;
    @Column(name = "shipping_history_name")
    private String shippingHistoryName;
    @Column(name = "shipping_history_index")
    private String shippingHistoryIndex;
    @Column(name = "shipping_history_country")
    private String shippingHistoryCountry;
    @Column(name = "shipping_history_created_at")
    private LocalDateTime shippingHistoryCreatedAt;
    @Column(name = "shipping_history_updated_at")
    private LocalDateTime shippingHistoryUpdatedAt;
    // shipping.delivery_service (id covered by shippingDeliveryServiceId above)
    @Column(name = "delivery_service_name")
    private String deliveryServiceName;
    @Column(name = "delivery_service_source_name")
    private String deliveryServiceSourceName;
    @Column(name = "delivery_service_alias")
    private String deliveryServiceAlias;

    // warehouse
    @Column(name = "warehouse_id")
    private Long warehouseId;
    @Column(name = "warehouse_name")
    private String warehouseName;
    @Column(name = "warehouse_description")
    private String warehouseDescription;
    @Column(name = "warehouse_is_active")
    private Boolean warehouseIsActive;
    @Column(name = "warehouse_is_default")
    private Boolean warehouseIsDefault;
    @Column(name = "warehouse_address_id")
    private Long warehouseAddressId;
    @Column(name = "warehouse_created_at")
    private LocalDateTime warehouseCreatedAt;
    @Column(name = "warehouse_updated_at")
    private LocalDateTime warehouseUpdatedAt;
    @Column(name = "warehouse_deleted_at")
    private LocalDateTime warehouseDeletedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductEntity> products = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderPaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTagEntity> tags = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderCustomFieldEntity> customFields = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderAssigneeEntity> assignees = new ArrayList<>();
}
