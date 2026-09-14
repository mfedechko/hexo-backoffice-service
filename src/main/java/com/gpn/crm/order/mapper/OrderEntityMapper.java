package com.gpn.crm.order.mapper;

import com.gpn.crm.keycrm.dto.KeyCrmAssignedUser;
import com.gpn.crm.keycrm.dto.KeyCrmBuyer;
import com.gpn.crm.keycrm.dto.KeyCrmCustomField;
import com.gpn.crm.keycrm.dto.KeyCrmDeliveryService;
import com.gpn.crm.keycrm.dto.KeyCrmManager;
import com.gpn.crm.keycrm.dto.KeyCrmMarketing;
import com.gpn.crm.keycrm.dto.KeyCrmOrder;
import com.gpn.crm.keycrm.dto.KeyCrmOrderProduct;
import com.gpn.crm.keycrm.dto.KeyCrmOrderProductOffer;
import com.gpn.crm.keycrm.dto.KeyCrmOrderStatus;
import com.gpn.crm.keycrm.dto.KeyCrmOrderWarehouse;
import com.gpn.crm.keycrm.dto.KeyCrmPayment;
import com.gpn.crm.keycrm.dto.KeyCrmShipping;
import com.gpn.crm.keycrm.dto.KeyCrmShippingHistory;
import com.gpn.crm.keycrm.dto.KeyCrmTag;
import com.gpn.crm.order.model.OrderAssigneeEntity;
import com.gpn.crm.order.model.OrderAssigneeId;
import com.gpn.crm.order.model.OrderCustomFieldEntity;
import com.gpn.crm.order.model.OrderCustomFieldId;
import com.gpn.crm.order.model.OrderEntity;
import com.gpn.crm.order.model.OrderPaymentEntity;
import com.gpn.crm.order.model.OrderProductEntity;
import com.gpn.crm.order.model.OrderTagEntity;
import com.gpn.crm.order.model.OrderTagId;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps the raw {@link KeyCrmOrder} tree onto the persistence entities backing
 * V5__add_orders_tables.sql (see {@link OrderEntity} for the column layout rationale).
 * Every child collection is rebuilt from scratch on each call and pointed back at the parent,
 * so saving the returned {@link OrderEntity} (cascade + orphanRemoval) fully replaces that
 * order's children - the intended behavior for a re-sync.
 */
@Component
public class OrderEntityMapper {

    /** KeyCRM's other, space-separated timestamp format (e.g. last_synced_at). */
    private static final DateTimeFormatter SPACE_SEPARATED = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderEntity toEntity(KeyCrmOrder order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.id());
        entity.setSourceUuid(order.sourceUuid());
        entity.setGlobalSourceUuid(order.globalSourceUuid());
        entity.setStatusOnSource(order.statusOnSource());
        entity.setSourceId(order.sourceId());
        entity.setClientId(order.clientId());
        entity.setGrandTotal(order.grandTotal());
        entity.setTotalDiscount(order.totalDiscount());
        entity.setMarginSum(order.marginSum());
        entity.setExpensesSum(order.expensesSum());
        entity.setDiscountAmount(order.discountAmount());
        entity.setDiscountPercent(order.discountPercent());
        entity.setShippingPrice(order.shippingPrice());
        entity.setTaxes(order.taxes());
        entity.setRegisterId(order.registerId());
        entity.setFiscalResult(order.fiscalResult());
        entity.setFiscalStatus(order.fiscalStatus());
        entity.setShippingTypeId(order.shippingTypeId());
        entity.setStatusGroupId(order.statusGroupId());
        entity.setStatusId(order.statusId());
        entity.setClosedFrom(order.closedFrom());
        entity.setStatusExpiredAt(parseDateTime(order.statusExpiredAt()));
        entity.setStatusChangedAt(parseDateTime(order.statusChangedAt()));
        entity.setParentId(order.parentId());
        entity.setManagerComment(order.managerComment());
        entity.setClientComment(order.clientComment());
        entity.setDiscountData(order.discountData());
        entity.setIsGift(order.isGift());
        entity.setPromocode(order.promocode());
        entity.setWrapPrice(order.wrapPrice());
        entity.setGiftWrap(order.giftWrap());
        entity.setPaymentStatus(order.paymentStatus());
        entity.setGiftMessage(order.giftMessage());
        entity.setLastSyncedAt(parseDateTime(order.lastSyncedAt()));
        entity.setCreatedAt(parseDateTime(order.createdAt()));
        entity.setUpdatedAt(parseDateTime(order.updatedAt()));
        entity.setClosedAt(parseDateTime(order.closedAt()));
        entity.setOrderedAt(parseDateTime(order.orderedAt()));
        entity.setSourceUpdatedAt(parseDateTime(order.sourceUpdatedAt()));
        entity.setDeletedAt(parseDateTime(order.deletedAt()));
        entity.setPaymentsTotal(order.paymentsTotal());
        entity.setProductsTotal(order.productsTotal());
        entity.setIsExpired(order.isExpired());
        entity.setHasReserves(order.hasReserves());
        entity.setBuyerComment(order.buyerComment());

        applyBuyer(entity, order.buyer());
        applyManager(entity, order.manager());
        applyStatus(entity, order.status());
        applyMarketing(entity, order.marketing());
        applyShipping(entity, order.shipping());
        applyWarehouse(entity, order.warehouse());

        entity.setProducts(toProducts(entity, order.products()));
        entity.setPayments(toPayments(entity, order.payments(), order.expenses()));
        entity.setTags(toTags(entity, order.tags()));
        entity.setCustomFields(toCustomFields(entity, order.customFields()));
        entity.setAssignees(toAssignees(entity, order.assigned()));

        return entity;
    }

    private void applyBuyer(OrderEntity entity, KeyCrmBuyer buyer) {
        if (buyer == null) {
            return;
        }
        entity.setBuyerId(buyer.id());
        entity.setBuyerCompanyId(buyer.companyId());
        entity.setBuyerFullName(buyer.fullName());
        entity.setBuyerBirthday(parseDateTime(buyer.birthday()));
        entity.setBuyerPhone(buyer.phone());
        entity.setBuyerEmail(buyer.email());
        entity.setBuyerNote(buyer.note());
        entity.setBuyerPicture(buyer.picture());
        entity.setBuyerImage(buyer.image());
        entity.setBuyerOrdersSum(buyer.ordersSum());
        entity.setBuyerDiscount(buyer.discount());
        entity.setBuyerCurrency(buyer.currency());
        entity.setBuyerOrdersCount(buyer.ordersCount());
        entity.setBuyerHasDuplicates(buyer.hasDuplicates());
        entity.setBuyerManagerId(buyer.managerId());
        entity.setBuyerDeletedAt(parseDateTime(buyer.deletedAt()));
        entity.setBuyerCreatedAt(parseDateTime(buyer.createdAt()));
        entity.setBuyerUpdatedAt(parseDateTime(buyer.updatedAt()));
    }

    private void applyManager(OrderEntity entity, KeyCrmManager manager) {
        if (manager == null) {
            return;
        }
        entity.setManagerId(manager.id());
        entity.setManagerFirstName(manager.firstName());
        entity.setManagerLastName(manager.lastName());
        entity.setManagerEmail(manager.email());
        entity.setManagerUsername(manager.username());
        entity.setManagerPhone(manager.phone());
        entity.setManagerRoleId(manager.roleId());
        entity.setManagerAvatarId(manager.avatarId());
        entity.setManagerStatus(manager.status());
        entity.setManagerLastLoggedAt(parseDateTime(manager.lastLoggedAt()));
        entity.setManagerReadyAt(parseDateTime(manager.readyAt()));
        entity.setManagerIsReady(manager.isReady());
        entity.setManagerUse2fa(manager.use2fa());
        entity.setManagerCreatedAt(parseDateTime(manager.createdAt()));
        entity.setManagerUpdatedAt(parseDateTime(manager.updatedAt()));
        entity.setManagerDeletedAt(parseDateTime(manager.deletedAt()));
        entity.setManagerFullName(manager.fullName());
    }

    private void applyStatus(OrderEntity entity, KeyCrmOrderStatus status) {
        if (status == null) {
            return;
        }
        entity.setStatusName(status.name());
        entity.setStatusAlias(status.alias());
        entity.setStatusIsActive(status.isActive());
        entity.setStatusIsClosingOrder(status.isClosingOrder());
        entity.setStatusIsReserved(status.isReserved());
        entity.setStatusExpirationPeriod(status.expirationPeriod());
        entity.setStatusCreatedAt(parseDateTime(status.createdAt()));
        entity.setStatusUpdatedAt(parseDateTime(status.updatedAt()));
        entity.setStatusDeletedAt(parseDateTime(status.deletedAt()));
    }

    private void applyMarketing(OrderEntity entity, KeyCrmMarketing marketing) {
        if (marketing == null) {
            return;
        }
        entity.setMarketingId(marketing.id());
        entity.setUtmSource(marketing.utmSource());
        entity.setUtmMedium(marketing.utmMedium());
        entity.setUtmCampaign(marketing.utmCampaign());
        entity.setUtmTerm(marketing.utmTerm());
        entity.setUtmContent(marketing.utmContent());
        entity.setMarketingCreatedAt(parseDateTime(marketing.createdAt()));
        entity.setMarketingUpdatedAt(parseDateTime(marketing.updatedAt()));
    }

    private void applyShipping(OrderEntity entity, KeyCrmShipping shipping) {
        if (shipping == null) {
            return;
        }
        entity.setShippingDeliveryServiceId(shipping.deliveryServiceId());
        entity.setShippingAddressId(shipping.addressId());
        entity.setShippingLastHistoryId(shipping.lastHistoryId());
        entity.setShippingTrackingCode(shipping.trackingCode());
        entity.setShippingReturnTrackingCode(shipping.returnTrackingCode());
        entity.setShippingTrackingCodeSendAt(parseDateTime(shipping.trackingCodeSendAt()));
        entity.setShippingStatus(shipping.shippingStatus());
        entity.setShippingShipmentPayload(shipping.shipmentPayload());
        entity.setShippingAddressPayload(shipping.addressPayload());
        entity.setShippingIsWarehouse(shipping.isWarehouse());
        entity.setShippingPreferredMethod(shipping.shippingPreferredMethod());
        entity.setShippingAddress(shipping.shippingAddress());
        entity.setShippingRecipientPhone(shipping.recipientPhone());
        entity.setShippingRecipientFullName(shipping.recipientFullName());
        entity.setShippingAddressCountry(shipping.shippingAddressCountry());
        entity.setShippingAddressCountryCode(shipping.shippingAddressCountryCode());
        entity.setShippingAddressRegion(shipping.shippingAddressRegion());
        entity.setShippingAddressCity(shipping.shippingAddressCity());
        entity.setShippingAddressZip(shipping.shippingAddressZip());
        entity.setShippingReceivePoint(shipping.shippingReceivePoint());
        entity.setShippingSecondaryLine(shipping.shippingSecondaryLine());
        entity.setShippingDate(parseDateTime(shipping.shippingDate()));
        entity.setShippingDateActual(parseDateTime(shipping.shippingDateActual()));
        entity.setShippingDateActualHasOwner(shipping.shippingDateActualHasOwner());
        entity.setShippingWasShipped(shipping.wasShipped());
        entity.setShippingCreatedAt(parseDateTime(shipping.createdAt()));
        entity.setShippingUpdatedAt(parseDateTime(shipping.updatedAt()));
        entity.setShippingDeletedAt(parseDateTime(shipping.deletedAt()));
        entity.setShippingFullAddress(shipping.fullAddress());

        KeyCrmShippingHistory history = shipping.lastHistory();
        if (history != null) {
            entity.setShippingHistoryLastOfficeIndex(history.lastOfficeIndex());
            entity.setShippingHistoryStatusCode(history.statusCode());
            entity.setShippingHistoryDescription(history.description());
            entity.setShippingHistoryStatus(history.shippingStatus());
            entity.setShippingHistoryDate(parseDateTime(history.shippingDate()));
            entity.setShippingHistoryName(history.name());
            entity.setShippingHistoryIndex(history.index());
            entity.setShippingHistoryCountry(history.country());
            entity.setShippingHistoryCreatedAt(parseDateTime(history.createdAt()));
            entity.setShippingHistoryUpdatedAt(parseDateTime(history.updatedAt()));
        }

        KeyCrmDeliveryService deliveryService = shipping.deliveryService();
        if (deliveryService != null) {
            entity.setDeliveryServiceName(deliveryService.name());
            entity.setDeliveryServiceSourceName(deliveryService.sourceName());
            entity.setDeliveryServiceAlias(deliveryService.alias());
        }
    }

    private void applyWarehouse(OrderEntity entity, KeyCrmOrderWarehouse warehouse) {
        if (warehouse == null) {
            return;
        }
        entity.setWarehouseId(warehouse.id());
        entity.setWarehouseName(warehouse.name());
        entity.setWarehouseDescription(warehouse.description());
        entity.setWarehouseIsActive(warehouse.isActive());
        entity.setWarehouseIsDefault(warehouse.isDefault());
        entity.setWarehouseAddressId(warehouse.addressId());
        entity.setWarehouseCreatedAt(parseDateTime(warehouse.createdAt()));
        entity.setWarehouseUpdatedAt(parseDateTime(warehouse.updatedAt()));
        entity.setWarehouseDeletedAt(parseDateTime(warehouse.deletedAt()));
    }

    private List<OrderProductEntity> toProducts(OrderEntity order, List<KeyCrmOrderProduct> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream().map(product -> toProduct(order, product)).toList();
    }

    private OrderProductEntity toProduct(OrderEntity order, KeyCrmOrderProduct product) {
        OrderProductEntity entity = new OrderProductEntity();
        entity.setId(product.id());
        entity.setOrder(order);
        entity.setSku(product.sku());
        entity.setVariationId(product.variationId());
        entity.setPublicationSourceUuid(product.publicationSourceUuid());
        entity.setName(product.name());
        entity.setUpsale(product.upsale());
        entity.setPrice(product.price());
        entity.setDiscountAmount(product.discountAmount());
        entity.setDiscountPercent(product.discountPercent());
        entity.setIndividualDiscount(product.individualDiscount());
        entity.setLoyaltyDiscount(product.loyaltyDiscount());
        entity.setTotalDiscount(product.totalDiscount());
        entity.setPurchasedPrice(product.purchasedPrice());
        entity.setPriceSold(product.priceSold());
        entity.setQuantity(product.quantity());
        entity.setUnitType(product.unitType());
        entity.setStockStatus(product.stockStatus());
        entity.setPicture(product.picture());
        entity.setComment(product.comment());
        entity.setProperties(product.properties());
        entity.setProductStatusId(product.productStatusId());
        entity.setCreatedAt(parseDateTime(product.createdAt()));
        entity.setUpdatedAt(parseDateTime(product.updatedAt()));
        entity.setShipmentType(product.shipmentType());

        KeyCrmOrderProductOffer offer = product.offer();
        if (offer != null) {
            entity.setOfferId(offer.id());
            entity.setOfferProductId(offer.productId());
            entity.setOfferSku(offer.sku());
            entity.setOfferBarcode(offer.barcode());
            entity.setOfferThumbnailUrl(offer.thumbnailUrl());
            entity.setOfferPrice(offer.price());
            entity.setOfferPurchasedPrice(offer.purchasedPrice());
            entity.setOfferQuantity(offer.quantity());
            entity.setOfferInReserve(offer.inReserve());
            entity.setOfferWeight(offer.weight());
            entity.setOfferLength(offer.length());
            entity.setOfferHeight(offer.height());
            entity.setOfferWidth(offer.width());
            entity.setOfferProperties(offer.properties());
            entity.setOfferIsDefault(offer.isDefault());
            entity.setOfferIsArchived(offer.isArchived());
            entity.setOfferCreatedAt(parseDateTime(offer.createdAt()));
            entity.setOfferUpdatedAt(parseDateTime(offer.updatedAt()));
        }

        KeyCrmOrderWarehouse warehouse = product.warehouse();
        if (warehouse != null) {
            entity.setWarehouseId(warehouse.id());
            entity.setWarehouseName(warehouse.name());
            entity.setWarehouseDescription(warehouse.description());
            entity.setWarehouseIsActive(warehouse.isActive());
            entity.setWarehouseIsDefault(warehouse.isDefault());
            entity.setWarehouseAddressId(warehouse.addressId());
            entity.setWarehouseCreatedAt(parseDateTime(warehouse.createdAt()));
            entity.setWarehouseUpdatedAt(parseDateTime(warehouse.updatedAt()));
            entity.setWarehouseDeletedAt(parseDateTime(warehouse.deletedAt()));
        }

        return entity;
    }

    private List<OrderPaymentEntity> toPayments(
            OrderEntity order, List<KeyCrmPayment> payments, List<KeyCrmPayment> expenses) {
        List<OrderPaymentEntity> result = new ArrayList<>();
        if (payments != null) {
            payments.forEach(payment -> result.add(toPayment(order, payment, false)));
        }
        if (expenses != null) {
            expenses.forEach(expense -> result.add(toPayment(order, expense, true)));
        }
        return result;
    }

    private OrderPaymentEntity toPayment(OrderEntity order, KeyCrmPayment payment, boolean isExpense) {
        OrderPaymentEntity entity = new OrderPaymentEntity();
        entity.setId(payment.id());
        entity.setOrder(order);
        entity.setDestinationId(payment.destinationId());
        entity.setDestinationType(payment.destinationType());
        entity.setAmount(payment.amount());
        entity.setSourceCurrency(payment.sourceCurrency());
        entity.setActualAmount(payment.actualAmount());
        entity.setActualCurrency(payment.actualCurrency());
        // is_expense also comes through on the raw payload; the include the row came from is
        // the source of truth, since a payment can be missing/null there.
        entity.setIsExpense(payment.isExpense() != null ? payment.isExpense() : isExpense);
        entity.setPaymentMethodId(payment.paymentMethodId());
        entity.setExpenseTypeId(payment.expenseTypeId());
        entity.setTransactionUuid(payment.transactionUuid());
        entity.setInvoiceUrl(payment.invoiceUrl());
        entity.setRegisterId(payment.registerId());
        entity.setBillId(payment.billId());
        entity.setBillType(payment.billType());
        entity.setRelatedPaymentId(payment.relatedPaymentId());
        entity.setFiscalResult(payment.fiscalResult());
        entity.setFiscalStatus(payment.fiscalStatus());
        entity.setDescription(payment.description());
        entity.setStatus(payment.status());
        entity.setPaymentDate(parseDateTime(payment.paymentDate()));
        entity.setCreatedAt(parseDateTime(payment.createdAt()));
        entity.setUpdatedAt(parseDateTime(payment.updatedAt()));
        return entity;
    }

    private List<OrderTagEntity> toTags(OrderEntity order, List<KeyCrmTag> tags) {
        if (tags == null) {
            return List.of();
        }
        return tags.stream().map(tag -> {
            OrderTagEntity entity = new OrderTagEntity();
            entity.setId(new OrderTagId(order.getId(), tag.id()));
            entity.setOrder(order);
            entity.setName(tag.name());
            entity.setAlias(tag.alias());
            entity.setColor(tag.color());
            entity.setCreatedAt(parseDateTime(tag.createdAt()));
            entity.setUpdatedAt(parseDateTime(tag.updatedAt()));
            return entity;
        }).toList();
    }

    private List<OrderCustomFieldEntity> toCustomFields(OrderEntity order, List<KeyCrmCustomField> customFields) {
        if (customFields == null) {
            return List.of();
        }
        return customFields.stream().map(field -> {
            OrderCustomFieldEntity entity = new OrderCustomFieldEntity();
            entity.setId(new OrderCustomFieldId(order.getId(), field.id()));
            entity.setOrder(order);
            entity.setUuid(field.uuid());
            entity.setName(field.name());
            entity.setType(field.type());
            entity.setValue(field.value());
            return entity;
        }).toList();
    }

    private List<OrderAssigneeEntity> toAssignees(OrderEntity order, List<KeyCrmAssignedUser> assigned) {
        if (assigned == null) {
            return List.of();
        }
        return assigned.stream().map(user -> {
            OrderAssigneeEntity entity = new OrderAssigneeEntity();
            entity.setId(new OrderAssigneeId(order.getId(), user.id()));
            entity.setOrder(order);
            entity.setFirstName(user.firstName());
            entity.setLastName(user.lastName());
            entity.setEmail(user.email());
            entity.setUsername(user.username());
            entity.setPhone(user.phone());
            entity.setAvatarId(user.avatarId());
            entity.setStatus(user.status());
            entity.setLastLoggedAt(parseDateTime(user.lastLoggedAt()));
            entity.setReadyAt(parseDateTime(user.readyAt()));
            entity.setIsReady(user.isReady());
            entity.setUse2fa(user.use2fa());
            entity.setCreatedAt(parseDateTime(user.createdAt()));
            entity.setUpdatedAt(parseDateTime(user.updatedAt()));
            entity.setDeletedAt(parseDateTime(user.deletedAt()));
            if (user.role() != null) {
                entity.setRoleId(user.role().id());
                entity.setRoleName(user.role().name());
                entity.setRoleAlias(user.role().alias());
                entity.setRoleColor(user.role().color());
            }
            entity.setFullName(user.fullName());
            entity.setAvatar(user.avatar());
            return entity;
        }).toList();
    }

    /**
     * KeyCRM's timestamp strings aren't consistently formatted - most fields are ISO-8601 with
     * a trailing Z and microseconds (e.g. order.created_at), but a few (e.g. last_synced_at)
     * are space-separated with no offset. Both are UTC.
     */
    private static LocalDateTime parseDateTime(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.ofInstant(Instant.parse(raw), ZoneOffset.UTC);
        } catch (DateTimeParseException isoFailure) {
            try {
                return LocalDateTime.parse(raw, SPACE_SEPARATED);
            } catch (DateTimeParseException spaceFailure) {
                return null;
            }
        }
    }
}
