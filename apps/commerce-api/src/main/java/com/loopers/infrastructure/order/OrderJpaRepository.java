package com.loopers.infrastructure.order;

import com.loopers.domain.command.order.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderLines.lines WHERE o.userId = :userId")
    Optional<Order> findByUserIdWithOrderLines(@Param("userId") Long userId);
}
