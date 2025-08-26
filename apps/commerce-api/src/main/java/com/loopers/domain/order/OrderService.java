package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "OrderService.findById(): 주문을 찾을 수 없습니다. 주문 ID: " + orderId));
    }

    @Transactional(readOnly = true)
    public Order findByOrderNumber(OrderNumber orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                "OrderService.findByOrderNumber(): 주문을 찾을 수 없습니다. 주문 번호: " + orderNumber.number()));
    }

    @Transactional
    public Order findByOrderNumberWithLock(OrderNumber orderNumber) {
        return orderRepository.findByOrderNumberWithLock(orderNumber)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                "OrderService.findByOrderNumberWithLock(): 주문을 찾을 수 없습니다. 주문 번호: " + orderNumber.number()));
    }

    @Transactional
    public Optional<Order> findByIdempotencyKeyWithLockOptional(IdempotencyKey idempotencyKey) {
        return orderRepository.findByIdempotencyKeyWithLock(idempotencyKey);
    }

    @Transactional
    public Optional<Order> findByIdempotencyKeyOptional(IdempotencyKey idempotencyKey) {
        return orderRepository.findByIdempotencyKey(idempotencyKey);
    }
}
