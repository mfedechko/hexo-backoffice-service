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

import java.time.LocalDateTime;

/**
 * Per-order tag join (V5__add_orders_tables.sql). KeyCRM tags are a reusable catalog; the
 * tag's own descriptive fields are copied alongside rather than deduped into a separate
 * {@code tags} table.
 */
@Entity
@Table(name = "order_tags")
@Getter
@Setter
public class OrderTagEntity {

    @EmbeddedId
    private OrderTagId id = new OrderTagId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    private String name;
    private String alias;
    private String color;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
