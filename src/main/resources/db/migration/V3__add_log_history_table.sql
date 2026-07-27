CREATE TABLE log_history (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users(id),
    module     VARCHAR(20)  NOT NULL,
    object_id  BIGINT       NOT NULL,
    action     VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL
);

CREATE INDEX idx_log_history_user_id ON log_history(user_id);
CREATE INDEX idx_log_history_created_at ON log_history(created_at);
