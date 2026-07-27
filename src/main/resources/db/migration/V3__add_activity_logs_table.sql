CREATE TABLE activity_logs (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users(id),
    module     VARCHAR(20)  NOT NULL,
    object_id  BIGINT       NOT NULL,
    action     VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL
);

CREATE INDEX idx_activity_logs_user_id ON activity_logs(user_id);
CREATE INDEX idx_activity_logs_created_at ON activity_logs(created_at);
