CREATE TABLE leads
(
    id         BIGSERIAL PRIMARY KEY,
    type       VARCHAR(32)  NOT NULL,
    name       VARCHAR(100) NOT NULL,
    phone      VARCHAR(20)  NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    comment    VARCHAR(255),
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);
