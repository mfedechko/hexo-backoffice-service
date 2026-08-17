CREATE TABLE employees
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name  VARCHAR(100),
    phone      VARCHAR(20),
    email      VARCHAR(100),
    birthday   TIMESTAMP,
    department VARCHAR(100),
    parent_id  BIGINT,
    status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);
