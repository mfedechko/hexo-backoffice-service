CREATE TABLE permissions (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE roles (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE roles_permissions (
    role_id       BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    department_id BIGINT,
    created_at    TIMESTAMP NOT NULL,
    updated_at    TIMESTAMP
);

CREATE TABLE users_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX idx_roles_permissions_permission_id ON roles_permissions(permission_id);
CREATE INDEX idx_users_roles_role_id ON users_roles(role_id);

-- Baseline roles and permissions
INSERT INTO permissions (name, description) VALUES
    ('leads:read',  'View leads'),
    ('leads:write', 'Create and update leads'),
    ('users:read',  'View backoffice users'),
    ('users:write', 'Manage backoffice users');

INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p WHERE r.name = 'ROLE_ADMIN';

INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p ON p.name IN ('leads:read', 'leads:write')
WHERE r.name = 'ROLE_USER';

-- Default admin user (username: admin, password: admin123 — rotate via ADMIN_PASSWORD in prod)
INSERT INTO users (username, email, password, created_at)
VALUES ('admin', 'admin@hexo.local', '$2y$10$.tFOiGTPNNdLntNPopLgFex78cHKIxBcFVz9f2qgPOTfcKIj7U7fG', now());

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name = 'ROLE_ADMIN' WHERE u.username = 'admin';
