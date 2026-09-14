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
public class OrderCustomFieldId implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "field_id")
    private Long fieldId;

    public OrderCustomFieldId(Long orderId, Long fieldId) {
        this.orderId = orderId;
        this.fieldId = fieldId;
    }
}
