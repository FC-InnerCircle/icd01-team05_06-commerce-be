-- Insert Orders
INSERT INTO orders (id, member_id, street_address, detail_address, postal_code, status, created_at, updated_at) VALUES
                                                                                                                    (1, 1001, '123 Main St', 'Apt 4B', '12345', 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (2, 1002, '456 Oak St', 'Suite 200', '67890', 'PROCESSING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (3, 1003, '789 Pine St', 'Unit 101', '54321', 'SHIPPED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (4, 1004, '321 Maple St', 'House 12', '98765', 'DELIVERED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (5, 1005, '654 Cedar St', 'Apt 7D', '12321', 'CANCELLED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (6, 1006, '987 Birch St', 'Suite 1A', '67812', 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (7, 1007, '321 Oak St', 'Unit 305', '24680', 'PROCESSING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (8, 1008, '654 Walnut St', 'Apt 22B', '13579', 'SHIPPED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (9, 1009, '123 Elm St', 'House 15', '98712', 'DELIVERED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                    (10, 1010, '456 Spruce St', 'Suite 10C', '13568', 'CANCELLED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Order Items
INSERT INTO order_items (id, order_id, item_id, quantity, price_at_purchase, created_at, updated_at) VALUES
                                                                                                         (1, 1, 501, 2, 29.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (2, 1, 502, 1, 49.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (3, 2, 503, 3, 19.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (4, 2, 504, 4, 99.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (5, 3, 505, 5, 39.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (6, 3, 506, 2, 59.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (7, 4, 507, 1, 25.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (8, 4, 508, 4, 14.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (9, 5, 509, 3, 9.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                         (10, 5, 510, 6, 89.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
