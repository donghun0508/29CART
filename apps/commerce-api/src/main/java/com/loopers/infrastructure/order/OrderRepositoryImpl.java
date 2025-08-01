package com.loopers.infrastructure.order;

import com.loopers.domain.command.order.Order;
import com.loopers.domain.command.order.OrderRepository;
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
    public Optional<Order> findByUserId(Long userId) {
        return orderJpaRepository.findByUserIdWithOrderLines(userId);
    }
}
