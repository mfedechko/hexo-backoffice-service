package com.gpn.crm.order.repository;

import com.gpn.crm.order.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Child rows (order_products, order_payments, order_tags, order_custom_fields,
 * order_assignees) aren't queried directly yet - they're managed through {@link OrderEntity}'s
 * cascaded collections. Add dedicated repositories for them if/when that's needed.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
