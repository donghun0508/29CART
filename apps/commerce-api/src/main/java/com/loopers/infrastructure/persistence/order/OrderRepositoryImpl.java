package com.loopers.infrastructure.persistence.order;

import com.loopers.domain.order.IdempotencyKey;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.order.OrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Optional<Order> findByOrderNumber(OrderNumber orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public Optional<Order> findByOrderNumberWithLock(OrderNumber orderNumber) {
        return orderJpaRepository.findByOrderNumberWithLock(orderNumber);
    }

    @Override
    public Optional<Order> findByIdempotencyKeyWithLock(IdempotencyKey idempotencyKey) {
        return orderJpaRepository.findByIdempotencyKeyWithLock(idempotencyKey);
    }

    @Override
    public Optional<Order> findByIdempotencyKey(IdempotencyKey idempotencyKey) {
        return orderJpaRepository.findByIdempotencyKey(idempotencyKey);
    }
}
