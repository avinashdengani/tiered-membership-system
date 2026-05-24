CREATE TABLE orders
(
    id          UUID PRIMARY KEY,
    user_id     UUID           NOT NULL REFERENCES users (id),
    order_value NUMERIC(10, 2) NOT NULL,
    version     BIGINT         NOT NULL DEFAULT 0,
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP      NOT NULL
);