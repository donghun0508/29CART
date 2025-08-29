package com.loopers.domain.order;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long orderId);

    Optional<Order> findByOrderNumber(OrderNumber orderNumber);

    Optional<Order> findByOrderNumberWithLock(OrderNumber orderNumber);

    Optional<Order> findByIdempotencyKey(IdempotencyKey idempotencyKey);

    boolean existsByIdempotencyKey(IdempotencyKey idempotencyKey);
}
