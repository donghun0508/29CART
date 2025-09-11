delete from card_payment;
delete from point_payment;
delete from payment;
delete from order_line;
delete from orders;
truncate table issued_coupon;
truncate table outbox_event;

delete from paymentgateway.payments;
delete from events.event_log;
delete from events.product_metrics;

INSERT INTO issued_coupon (aggregate_id, name, code, target_scope, target_id, issued_at, expired_at, discount_type, discount_value, status, created_at, updated_at)
VALUES ('issued_coupon_agg1', '나이키 쿠폰', 'NIKE2024', 'USER', (SELECT id FROM users WHERE account_id = '135135'), '2025-01-23 12:53:26', '3000-12-31 23:59:59', 'FIXED', 5000, 'AVAILABLE', NOW(), NOW());
