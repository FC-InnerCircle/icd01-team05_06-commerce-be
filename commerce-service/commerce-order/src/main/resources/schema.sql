DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS member;

-- Member 테이블 생성
CREATE TABLE member (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        phone VARCHAR(255) NOT NULL,
                        last_login_date DATETIME,
                        refresh_token VARCHAR(255),
                        deleted_at DATETIME,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Orders 테이블 생성
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        member_id BIGINT NOT NULL,
                        street_address VARCHAR(255) NOT NULL,
                        detail_address VARCHAR(255) NOT NULL,
                        postal_code VARCHAR(255) NOT NULL,
                        order_number VARCHAR(255) NOT NULL,
                        payment_method VARCHAR(255) NOT NULL,
                        recipient VARCHAR(255) NOT NULL,
                        content TEXT NOT NULL,
                        discounted_price DECIMAL(19,2) NOT NULL,
                        price DECIMAL(19,2) NOT NULL,
                        status VARCHAR(255) NOT NULL,
                        order_date DATETIME NOT NULL,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (member_id) REFERENCES member(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- OrderProduct 테이블 생성
CREATE TABLE order_product (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               order_id BIGINT,
                               product_id BIGINT NOT NULL,
                               quantity BIGINT NOT NULL,
                               price DECIMAL(19,2) NOT NULL,
                               discounted_price DECIMAL(19,2) NOT NULL,
                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (order_id) REFERENCES orders(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;