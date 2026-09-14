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
 * One ledger entry attached to an order (V5__add_orders_tables.sql) - shared by KeyCRM's
 * payments and expenses relations (identical shape; {@code isExpense} tells them apart).
 * {@code id} is KeyCRM's own payment/expense id.
 */
@Entity
@Table(name = "order_payments")
@Getter
@Setter
public class OrderPaymentEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "destination_id")
    private Long destinationId;
    @Column(name = "destination_type")
    private String destinationType;
    private BigDecimal amount;
    @Column(name = "source_currency")
    private String sourceCurrency;
    @Column(name = "actual_amount")
    private BigDecimal actualAmount;
    @Column(name = "actual_currency")
    private String actualCurrency;
    @Column(name = "is_expense", nullable = false)
    private Boolean isExpense;
    @Column(name = "payment_method_id")
    private Long paymentMethodId;
    @Column(name = "expense_type_id")
    private Long expenseTypeId;
    @Column(name = "transaction_uuid")
    private String transactionUuid;
    @Column(name = "invoice_url")
    private String invoiceUrl;
    @Column(name = "register_id")
    private Long registerId;
    @Column(name = "bill_id")
    private Long billId;
    @Column(name = "bill_type")
    private String billType;
    @Column(name = "related_payment_id")
    private Long relatedPaymentId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "fiscal_result", columnDefinition = "jsonb")
    private Object fiscalResult;
    @Column(name = "fiscal_status")
    private String fiscalStatus;
    private String description;
    private String status;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
