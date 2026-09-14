package com.gpn.crm.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * One order line item (V5__add_orders_tables.sql). {@code id} is KeyCRM's own order-product
 * line id. The line's offer (variant) and fulfilling warehouse are flattened in as prefixed
 * columns, same approach as {@link OrderEntity}.
 */
@Entity
@Table(name = "order_products")
@Getter
@Setter
public class OrderProductEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    private String sku;
    @Column(name = "variation_id")
    private Long variationId;
    @Column(name = "publication_source_uuid")
    private String publicationSourceUuid;
    private String name;
    private Boolean upsale;
    private BigDecimal price;
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;
    @Column(name = "discount_percent")
    private BigDecimal discountPercent;
    @Column(name = "individual_discount")
    private BigDecimal individualDiscount;
    @Column(name = "loyalty_discount")
    private BigDecimal loyaltyDiscount;
    @Column(name = "total_discount")
    private BigDecimal totalDiscount;
    @Column(name = "purchased_price")
    private BigDecimal purchasedPrice;
    @Column(name = "price_sold")
    private BigDecimal priceSold;
    private BigDecimal quantity;
    @Column(name = "unit_type")
    private String unitType;
    @Column(name = "stock_status")
    private String stockStatus;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object picture;
    private String comment;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object properties;
    @Column(name = "product_status_id")
    private Long productStatusId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "shipment_type")
    private String shipmentType;

    // offer (variant) sold on this line
    @Column(name = "offer_id")
    private Long offerId;
    @Column(name = "offer_product_id")
    private Long offerProductId;
    @Column(name = "offer_sku")
    private String offerSku;
    @Column(name = "offer_barcode")
    private String offerBarcode;
    @Column(name = "offer_thumbnail_url")
    private String offerThumbnailUrl;
    @Column(name = "offer_price")
    private BigDecimal offerPrice;
    @Column(name = "offer_purchased_price")
    private BigDecimal offerPurchasedPrice;
    @Column(name = "offer_quantity")
    private BigDecimal offerQuantity;
    @Column(name = "offer_in_reserve")
    private BigDecimal offerInReserve;
    @Column(name = "offer_weight")
    private BigDecimal offerWeight;
    @Column(name = "offer_length")
    private BigDecimal offerLength;
    @Column(name = "offer_height")
    private BigDecimal offerHeight;
    @Column(name = "offer_width")
    private BigDecimal offerWidth;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "offer_properties", columnDefinition = "jsonb")
    private Object offerProperties;
    @Column(name = "offer_is_default")
    private Boolean offerIsDefault;
    @Column(name = "offer_is_archived")
    private Boolean offerIsArchived;
    @Column(name = "offer_created_at")
    private LocalDateTime offerCreatedAt;
    @Column(name = "offer_updated_at")
    private LocalDateTime offerUpdatedAt;

    // warehouse this line was fulfilled from
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
}
