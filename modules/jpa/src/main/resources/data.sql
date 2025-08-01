-- User 테스트 데이터 INSERT (3명)
INSERT INTO users (user_id, email, birth, gender, created_at, updated_at)
VALUES ('test', 'john.doe@example.com', '1995-03-15', 'M', NOW(), NOW()),
       ('alice456', 'alice.smith@example.com', '1992-07-22', 'F', NOW(), NOW()),
       ('bob789', 'bob.wilson@example.com', '1988-11-08', 'M', NOW(), NOW());

INSERT INTO point (user_id, balance, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE user_id = 'test'), 0, NOW(), NOW()),
       ((SELECT id FROM users WHERE user_id = 'alice456'), 50000, NOW(), NOW()),
       ((SELECT id FROM users WHERE user_id = 'bob789'), 25000, NOW(), NOW());


INSERT INTO brand (name, created_at, updated_at)
VALUES ('Luminous', NOW(), NOW()),
       ('UrbanPeak', NOW(), NOW()),
       ('NovaStyle', NOW(), NOW()),
       ('EchoCraft', NOW(), NOW()),
       ('VividEra', NOW(), NOW());

INSERT INTO product (brand_id, name, price, stock, created_at, updated_at)
VALUES (1, 'Alpha Bag', 15000, 100, '2024-12-15 14:32:10', now()),
       (1, 'Beta Shoes', 25000, 50, '2024-11-28 09:15:33', now()),
       (1, 'Gamma Watch', 8500, 0, '2025-01-03 16:47:22', now()),
       (1, 'Delta Jacket', 45000, 30, '2024-10-12 11:28:45', now()),
       (1, 'Echo Hat', 12000, 0, '2024-12-31 08:12:17', now()),
       (1, 'Foxtrot Shirt', 35000, 80, '2024-09-22 13:55:29', now()),
       (1, 'Golf Pants', 7500, 300, '2025-01-18 10:03:44', now()),
       (1, 'Hotel Socks', 28000, 60, '2024-11-05 15:41:18', now()),
       (1, 'India Glasses', 18500, 120, '2024-12-08 12:29:56', now()),
       (1, 'Juliet Belt', 52000, 25, '2024-10-30 17:14:33', now()),
       (1, 'Kilo Scarf', 9500, 180, '2025-01-12 09:38:27', now()),
       (1, 'Lima Coat', 33000, 70, '2024-11-19 14:51:42', now()),
       (1, 'Mike Wallet', 21000, 90, '2024-12-23 11:07:15', now()),
       (1, 'November Cap', 67000, 15, '2024-09-18 16:33:48', now()),
       (1, 'Oscar Tie', 14500, 110, '2025-01-07 13:19:52', now()),
       (1, 'Papa Shoes', 39000, 40, '2024-10-25 08:45:36', now()),
       (1, 'Quebec Bag', 11000, 160, '2024-12-02 15:22:19', now()),
       (1, 'Romeo Watch', 48000, 35, '2024-11-14 12:08:43', now()),
       (1, 'Sierra Jacket', 16500, 130, '2025-01-25 09:55:17', now()),
       (1, 'Tango Hat', 29500, 55, '2024-10-08 14:42:31', now()),
       (1, 'Uniform Shirt', 22000, 85, '2024-12-19 11:28:54', now()),
       (1, 'Victor Pants', 41000, 45, '2024-09-29 16:15:22', now()),
       (1, 'Whiskey Socks', 13500, 140, '2025-01-15 13:41:38', now()),
       (1, 'Xray Glasses', 36500, 65, '2024-11-23 10:17:46', now()),
       (1, 'Yankee Belt', 19000, 105, '2024-12-12 15:53:29', now()),
       (1, 'Zulu Scarf', 54000, 20, '2024-10-17 12:39:14', now()),
       (1, 'Echo Coat', 10500, 170, '2025-01-21 08:26:37', now()),
       (1, 'Foxtrot Wallet', 31000, 75, '2024-11-08 14:12:51', now()),
       (1, 'Golf Cap', 24500, 95, '2024-12-27 11:48:25', now()),
       (1, 'Hotel Tie', 43500, 38, '2024-09-14 16:34:19', now()),
       (1, 'India Shoes', 17500, 125, '2025-01-09 13:21:43', now()),
       (1, 'Juliet Bag', 26500, 82, '2024-10-21 09:57:28', now()),
       (1, 'Kilo Watch', 38500, 52, '2024-12-05 15:44:12', now()),
       (1, 'Lima Jacket', 20500, 98, '2024-11-26 12:30:36', now()),
       (1, 'Mike Hat', 49500, 28, '2025-01-29 10:16:49', now()),
       (1, 'November Shirt', 12500, 155, '2024-09-25 14:03:23', now()),
       (1, 'Oscar Pants', 34500, 68, '2024-12-16 11:49:57', now()),
       (1, 'Papa Socks', 27500, 88, '2024-10-13 16:36:41', now()),
       (1, 'Quebec Glasses', 46500, 32, '2025-01-04 13:22:15', now()),
       (1, 'Romeo Belt', 15500, 135, '2024-11-11 09:08:38', now()),
       (1, 'Sierra Scarf', 37500, 58, '2024-12-29 15:55:22', now()),
       (1, 'Tango Coat', 23500, 92, '2024-10-06 12:41:46', now()),
       (1, 'Uniform Wallet', 51500, 22, '2025-01-17 08:28:19', now()),
       (1, 'Victor Cap', 14000, 145, '2024-11-20 14:14:33', now()),
       (1, 'Whiskey Tie', 32500, 72, '2024-12-21 11:01:57', now()),
       (1, 'Xray Shoes', 18000, 115, '2024-09-11 16:47:21', now()),
       (1, 'Yankee Bag', 44000, 42, '2025-01-11 13:34:45', now()),
       (1, 'Zulu Watch', 16000, 128, '2024-10-28 10:20:38', now()),
       (1, 'Alpha Jacket', 30000, 78, '2024-12-07 15:07:12', now()),
       (1, 'Beta Hat', 25500, 102, '2025-01-23 12:53:26', now());

INSERT INTO heart (user_id, target_id, target_type, created_at, updated_at)
VALUES
    ((SELECT id FROM users WHERE user_id = 'test'), (SELECT id FROM product WHERE brand_id = 1 AND name = 'Alpha Bag' LIMIT 1), 'PRODUCT', NOW(), NOW()),
    ((SELECT id FROM users WHERE user_id = 'alice456'), (SELECT id FROM product WHERE brand_id = 1 AND name = 'Alpha Bag' LIMIT 1),'PRODUCT',NOW(),NOW()),
    ((SELECT id FROM users WHERE user_id = 'test'), (SELECT id FROM product WHERE brand_id = 1 AND name = 'Beta Shoes' LIMIT 1),'PRODUCT',NOW(),NOW()),
    ((SELECT id FROM users WHERE user_id = 'alice456'), (SELECT id FROM product WHERE brand_id = 1 AND name = 'Beta Shoes' LIMIT 1),'PRODUCT',NOW(),NOW())
;