CREATE TABLE orders
(
    id          UUID PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
