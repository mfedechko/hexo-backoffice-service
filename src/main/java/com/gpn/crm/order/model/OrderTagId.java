package com.gpn.crm.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class OrderTagId implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "tag_id")
    private Long tagId;

    public OrderTagId(Long orderId, Long tagId) {
        this.orderId = orderId;
        this.tagId = tagId;
    }
}
