-- Mirrors the KeyCRM order shape modeled in com.gpn.crm.keycrm.dto (KeyCrmOrder and friends),
-- so orders synced from KeyCRM can be queried locally instead of re-fetched every time.
--
-- Layout: orders carries every order-level field, with its single-object relations (buyer,
-- manager, status, marketing, shipping incl. last_history/delivery_service, warehouse)
-- flattened in as prefixed columns. Its list relations each get their own child table
-- (order_products, order_payments, order_tags, order_custom_fields, order_assignees).
--
-- order_products.properties/offer_properties and the couple of JSONB columns below are kept
-- as JSONB rather than further child tables - either genuinely variable-shaped (KeyCRM casts
-- an empty relation to [] instead of null/{} for these) or a small nested key/value list not
-- worth another join level yet.
--
-- No indexes beyond primary keys/FKs yet, by design - added once real query patterns emerge.

CREATE TABLE orders
(
    id                              BIGINT PRIMARY KEY, -- KeyCRM's own order id
    source_uuid                     VARCHAR(100),
    global_source_uuid              VARCHAR(100),
    status_on_source                VARCHAR(100),
    source_id                       BIGINT,
    client_id                       BIGINT,
    grand_total                     NUMERIC(14, 2),
    total_discount                  NUMERIC(14, 2),
    margin_sum                      NUMERIC(14, 2),
    expenses_sum                    NUMERIC(14, 2),
    discount_amount                 NUMERIC(14, 2),
    discount_percent                NUMERIC(14, 2),
    shipping_price                  NUMERIC(14, 2),
    taxes                           JSONB,
    register_id                     BIGINT,
    fiscal_result                   JSONB,
    fiscal_status                   VARCHAR(50),
    shipping_type_id                BIGINT,
    status_group_id                 BIGINT,
    status_id                       BIGINT,
    closed_from                     VARCHAR(50),
    status_expired_at               TIMESTAMP,
    status_changed_at               TIMESTAMP,
    parent_id                       BIGINT,
    manager_comment                 VARCHAR(1000),
    client_comment                  VARCHAR(1000),
    discount_data                   JSONB,
    is_gift                         BOOLEAN,
    promocode                       VARCHAR(100),
    wrap_price                      NUMERIC(14, 2),
    gift_wrap                       BOOLEAN,
    payment_status                  VARCHAR(50),
    gift_message                    VARCHAR(1000),
    last_synced_at                  TIMESTAMP,
    created_at                      TIMESTAMP,
    updated_at                      TIMESTAMP,
    closed_at                       TIMESTAMP,
    ordered_at                      TIMESTAMP,
    source_updated_at               TIMESTAMP,
    deleted_at                      TIMESTAMP,
    payments_total                  NUMERIC(14, 2),
    products_total                  NUMERIC(14, 2),
    is_expired                      BOOLEAN,
    has_reserves                    BOOLEAN,
    buyer_comment                   VARCHAR(1000),

    -- buyer
    buyer_id                        BIGINT,
    buyer_company_id                BIGINT,
    buyer_full_name                 VARCHAR(255),
    buyer_birthday                  TIMESTAMP,
    buyer_phone                     VARCHAR(30),
    buyer_email                     VARCHAR(255),
    buyer_note                      VARCHAR(1000),
    buyer_picture                   VARCHAR(500),
    buyer_image                     VARCHAR(500),
    buyer_orders_sum                NUMERIC(14, 2),
    buyer_discount                  NUMERIC(14, 2),
    buyer_currency                  VARCHAR(10),
    buyer_orders_count              INTEGER,
    buyer_has_duplicates            INTEGER,
    buyer_manager_id                BIGINT,
    buyer_deleted_at                TIMESTAMP,
    buyer_created_at                TIMESTAMP,
    buyer_updated_at                TIMESTAMP,

    -- manager
    manager_id                      BIGINT,
    manager_first_name              VARCHAR(100),
    manager_last_name               VARCHAR(100),
    manager_email                   VARCHAR(255),
    manager_username                VARCHAR(100),
    manager_phone                   VARCHAR(30),
    manager_role_id                 BIGINT,
    manager_avatar_id               BIGINT,
    manager_status                  VARCHAR(30),
    manager_last_logged_at          TIMESTAMP,
    manager_ready_at                TIMESTAMP,
    manager_is_ready                BOOLEAN,
    manager_use_2fa                 BOOLEAN,
    manager_created_at              TIMESTAMP,
    manager_updated_at              TIMESTAMP,
    manager_deleted_at              TIMESTAMP,
    manager_full_name               VARCHAR(255),

    -- status catalog entry (id/group_id already covered by status_id/status_group_id above)
    status_name                     VARCHAR(100),
    status_alias                    VARCHAR(100),
    status_is_active                BOOLEAN,
    status_is_closing_order         BOOLEAN,
    status_is_reserved              BOOLEAN,
    status_expiration_period        BIGINT,
    status_created_at               TIMESTAMP,
    status_updated_at               TIMESTAMP,
    status_deleted_at               TIMESTAMP,

    -- marketing / UTM attribution
    marketing_id                    BIGINT,
    utm_source                      VARCHAR(255),
    utm_medium                      VARCHAR(255),
    utm_campaign                    VARCHAR(255),
    utm_term                        VARCHAR(255),
    utm_content                     VARCHAR(255),
    marketing_created_at            TIMESTAMP,
    marketing_updated_at            TIMESTAMP,

    -- shipping (shipping_price already covered by the order-level column above)
    shipping_delivery_service_id    BIGINT, -- also delivery_service.id
    shipping_address_id             BIGINT,
    shipping_last_history_id        BIGINT, -- also last_history.id
    shipping_tracking_code          VARCHAR(100),
    shipping_return_tracking_code   VARCHAR(100),
    shipping_tracking_code_send_at  TIMESTAMP,
    shipping_status                 VARCHAR(50),
    shipping_shipment_payload       JSONB, -- [] when empty, an object otherwise (KeyCRM quirk)
    shipping_address_payload        JSONB, -- [] when empty, an object otherwise (KeyCRM quirk)
    shipping_is_warehouse           BOOLEAN,
    shipping_preferred_method       VARCHAR(100),
    shipping_address                VARCHAR(500),
    shipping_recipient_phone        VARCHAR(30),
    shipping_recipient_full_name    VARCHAR(255),
    shipping_address_country        VARCHAR(100),
    shipping_address_country_code   VARCHAR(10),
    shipping_address_region         VARCHAR(255),
    shipping_address_city           VARCHAR(255),
    shipping_address_zip            VARCHAR(20),
    shipping_receive_point          VARCHAR(255),
    shipping_secondary_line         VARCHAR(255),
    shipping_date                   TIMESTAMP,
    shipping_date_actual            TIMESTAMP,
    shipping_date_actual_has_owner  BOOLEAN,
    shipping_was_shipped            BOOLEAN,
    shipping_created_at             TIMESTAMP,
    shipping_updated_at             TIMESTAMP,
    shipping_deleted_at             TIMESTAMP,
    shipping_full_address           VARCHAR(500),
    -- shipping.last_history (id covered by shipping_last_history_id above)
    shipping_history_last_office_index BIGINT,
    shipping_history_status_code    VARCHAR(20),
    shipping_history_description    VARCHAR(1000),
    shipping_history_status         VARCHAR(50),
    shipping_history_date           TIMESTAMP,
    shipping_history_name           VARCHAR(255),
    shipping_history_index          VARCHAR(50),
    shipping_history_country        VARCHAR(100),
    shipping_history_created_at     TIMESTAMP,
    shipping_history_updated_at     TIMESTAMP,
    -- shipping.delivery_service (id covered by shipping_delivery_service_id above)
    delivery_service_name           VARCHAR(255),
    delivery_service_source_name    VARCHAR(100),
    delivery_service_alias          VARCHAR(100),

    -- warehouse
    warehouse_id                    BIGINT,
    warehouse_name                  VARCHAR(255),
    warehouse_description           VARCHAR(500),
    warehouse_is_active             BOOLEAN,
    warehouse_is_default            BOOLEAN,
    warehouse_address_id            BIGINT,
    warehouse_created_at            TIMESTAMP,
    warehouse_updated_at            TIMESTAMP,
    warehouse_deleted_at            TIMESTAMP
);

CREATE TABLE order_products
(
    id                       BIGINT PRIMARY KEY, -- KeyCRM's own order-product line id
    order_id                 BIGINT      NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    sku                      VARCHAR(100),
    variation_id             BIGINT,
    publication_source_uuid  VARCHAR(100),
    name                     VARCHAR(500),
    upsale                   BOOLEAN,
    price                    NUMERIC(14, 2),
    discount_amount          NUMERIC(14, 2),
    discount_percent         NUMERIC(14, 2),
    individual_discount      NUMERIC(14, 2),
    loyalty_discount         NUMERIC(14, 2),
    total_discount           NUMERIC(14, 2),
    purchased_price          NUMERIC(14, 2),
    price_sold               NUMERIC(14, 2),
    quantity                 NUMERIC(14, 3),
    unit_type                VARCHAR(50),
    stock_status             VARCHAR(50),
    picture                  JSONB, -- [] when empty, an object otherwise (KeyCRM quirk)
    comment                  VARCHAR(1000),
    properties                JSONB, -- [{"name": ..., "value": ...}, ...]
    product_status_id        BIGINT,
    created_at                TIMESTAMP,
    updated_at                TIMESTAMP,
    shipment_type             VARCHAR(50),

    -- offer (variant) sold on this line
    offer_id                  BIGINT,
    offer_product_id          BIGINT,
    offer_sku                 VARCHAR(100),
    offer_barcode              VARCHAR(100),
    offer_thumbnail_url        VARCHAR(500),
    offer_price                NUMERIC(14, 2),
    offer_purchased_price      NUMERIC(14, 2),
    offer_quantity             NUMERIC(14, 3),
    offer_in_reserve           NUMERIC(14, 3),
    offer_weight               NUMERIC(10, 3),
    offer_length               NUMERIC(10, 3),
    offer_height               NUMERIC(10, 3),
    offer_width                NUMERIC(10, 3),
    offer_properties           JSONB, -- [{"name": ..., "value": ...}, ...]
    offer_is_default           BOOLEAN,
    offer_is_archived          BOOLEAN,
    offer_created_at           TIMESTAMP,
    offer_updated_at           TIMESTAMP,

    -- warehouse this line was fulfilled from
    warehouse_id                BIGINT,
    warehouse_name               VARCHAR(255),
    warehouse_description        VARCHAR(500),
    warehouse_is_active          BOOLEAN,
    warehouse_is_default         BOOLEAN,
    warehouse_address_id         BIGINT,
    warehouse_created_at         TIMESTAMP,
    warehouse_updated_at         TIMESTAMP,
    warehouse_deleted_at         TIMESTAMP
);

-- Shared by KeyCRM's payments and expenses relations (identical shape; is_expense tells them apart).
CREATE TABLE order_payments
(
    id                   BIGINT PRIMARY KEY, -- KeyCRM's own payment/expense id
    order_id             BIGINT       NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    destination_id       BIGINT,
    destination_type     VARCHAR(50),
    amount               NUMERIC(14, 2),
    source_currency      VARCHAR(10),
    actual_amount        NUMERIC(14, 2),
    actual_currency      VARCHAR(10),
    is_expense           BOOLEAN      NOT NULL,
    payment_method_id    BIGINT,
    expense_type_id      BIGINT,
    transaction_uuid     VARCHAR(100),
    invoice_url          VARCHAR(500),
    register_id          BIGINT,
    bill_id              BIGINT,
    bill_type            VARCHAR(50),
    related_payment_id   BIGINT,
    fiscal_result        JSONB,
    fiscal_status        VARCHAR(50),
    description          VARCHAR(1000),
    status               VARCHAR(50),
    payment_date         TIMESTAMP,
    created_at           TIMESTAMP,
    updated_at           TIMESTAMP
);

-- KeyCRM tags are a reusable catalog; this is the per-order join, with the tag's own
-- descriptive fields copied alongside rather than deduped into a separate tags table.
CREATE TABLE order_tags
(
    order_id   BIGINT NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    tag_id     BIGINT NOT NULL,
    name       VARCHAR(100),
    alias      VARCHAR(100),
    color      VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (order_id, tag_id)
);

-- KeyCRM custom fields are a reusable catalog of field definitions; this is the per-order
-- value for each field the order carries.
CREATE TABLE order_custom_fields
(
    order_id BIGINT NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    field_id BIGINT NOT NULL,
    uuid     VARCHAR(50),
    name     VARCHAR(255),
    type     VARCHAR(50),
    value    JSONB,
    PRIMARY KEY (order_id, field_id)
);

-- Users assigned to an order; like order_tags, the user's own descriptive fields are copied
-- alongside rather than deduped into a separate users table.
CREATE TABLE order_assignees
(
    order_id       BIGINT NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    user_id        BIGINT NOT NULL,
    first_name     VARCHAR(100),
    last_name      VARCHAR(100),
    email          VARCHAR(255),
    username       VARCHAR(100),
    phone          VARCHAR(30),
    avatar_id      BIGINT,
    status         VARCHAR(30),
    last_logged_at TIMESTAMP,
    ready_at       TIMESTAMP,
    is_ready       BOOLEAN,
    use_2fa        BOOLEAN,
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    deleted_at     TIMESTAMP,
    role_id        BIGINT,
    role_name      VARCHAR(100),
    role_alias     VARCHAR(100),
    role_color     VARCHAR(20),
    full_name      VARCHAR(255),
    avatar         JSONB, -- always null in every order observed so far; shape unverified
    PRIMARY KEY (order_id, user_id)
);
