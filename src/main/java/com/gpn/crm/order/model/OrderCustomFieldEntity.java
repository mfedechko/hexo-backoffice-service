package com.gpn.crm.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Per-order custom field value (V5__add_orders_tables.sql). KeyCRM custom fields are a
 * reusable catalog of field definitions; this is the value one order carries for a given field.
 */
@Entity
@Table(name = "order_custom_fields")
@Getter
@Setter
public class OrderCustomFieldEntity {

    @EmbeddedId
    private OrderCustomFieldId id = new OrderCustomFieldId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    private String uuid;
    private String name;
    private String type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object value;
}
