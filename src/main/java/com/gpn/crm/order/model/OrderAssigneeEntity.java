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

import java.time.LocalDateTime;

/**
 * A user assigned to an order (V5__add_orders_tables.sql). Like {@link OrderTagEntity}, the
 * user's own descriptive fields are copied alongside rather than deduped into a separate users
 * table.
 */
@Entity
@Table(name = "order_assignees")
@Getter
@Setter
public class OrderAssigneeEntity {

    @EmbeddedId
    private OrderAssigneeId id = new OrderAssigneeId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String username;
    private String phone;
    @Column(name = "avatar_id")
    private Long avatarId;
    private String status;
    @Column(name = "last_logged_at")
    private LocalDateTime lastLoggedAt;
    @Column(name = "ready_at")
    private LocalDateTime readyAt;
    @Column(name = "is_ready")
    private Boolean isReady;
    @Column(name = "use_2fa")
    private Boolean use2fa;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "role_alias")
    private String roleAlias;
    @Column(name = "role_color")
    private String roleColor;
    @Column(name = "full_name")
    private String fullName;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object avatar;
}
