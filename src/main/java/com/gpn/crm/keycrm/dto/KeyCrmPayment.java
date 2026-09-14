package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Raw shape of a ledger entry attached to an order, from GET /order?include=payments and
 * include=expenses (verified against a live call). Both relations share this exact shape -
 * {@code is_expense} distinguishes a payment from an expense.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmPayment(
        Long id,
        @JsonProperty("destination_id") Long destinationId,
        @JsonProperty("destination_type") String destinationType,
        BigDecimal amount,
        @JsonProperty("source_currency") String sourceCurrency,
        @JsonProperty("actual_amount") BigDecimal actualAmount,
        @JsonProperty("actual_currency") String actualCurrency,
        @JsonProperty("is_expense") Boolean isExpense,
        @JsonProperty("payment_method_id") Long paymentMethodId,
        @JsonProperty("expense_type_id") Long expenseTypeId,
        @JsonProperty("transaction_uuid") String transactionUuid,
        @JsonProperty("invoice_url") String invoiceUrl,
        @JsonProperty("register_id") Long registerId,
        @JsonProperty("bill_id") Long billId,
        @JsonProperty("bill_type") String billType,
        @JsonProperty("related_payment_id") Long relatedPaymentId,
        /** Always [] in every entry observed so far; element shape unverified. */
        @JsonProperty("fiscal_result") List<Object> fiscalResult,
        @JsonProperty("fiscal_status") String fiscalStatus,
        String description,
        String status,
        @JsonProperty("payment_date") String paymentDate,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}
