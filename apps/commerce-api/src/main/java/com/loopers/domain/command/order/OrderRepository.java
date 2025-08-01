package com.loopers.domain.command.order;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);
    Optional<Order> findByUserId(Long userId);
}
