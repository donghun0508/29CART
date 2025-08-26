package com.loopers.domain.payment;

import com.loopers.domain.order.OrderNumber;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderNumber(OrderNumber orderNumber);

    List<Payment> findAllByStatus(PaymentStatus status);
}
