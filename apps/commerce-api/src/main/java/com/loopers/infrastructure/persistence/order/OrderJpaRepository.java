package com.loopers.infrastructure.persistence.order;

import com.loopers.domain.order.IdempotencyKey;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderNumber;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(OrderNumber orderNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.orderNumber = :orderNumber")
    Optional<Order> findByOrderNumberWithLock(@Param("orderNumber") OrderNumber orderNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.idempotencyKey = :idempotencyKey")
    Optional<Order> findByIdempotencyKeyWithLock(@Param("idempotencyKey") IdempotencyKey idempotencyKey);

    List<Order> id(Long id);

    Optional<Order> findByIdempotencyKey(IdempotencyKey idempotencyKey);
}
