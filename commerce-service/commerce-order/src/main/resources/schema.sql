CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        member_id BIGINT NOT NULL,
                        street_address VARCHAR(255),
                        detail_address VARCHAR(255),
                        postal_code VARCHAR(20),
                        status VARCHAR(20),
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP
);

CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT,
                             item_id BIGINT,
                             quantity INT,
                             price_at_purchase DECIMAL(19, 2),
                             created_at TIMESTAMP,
                             updated_at TIMESTAMP,
                             CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id)
);
