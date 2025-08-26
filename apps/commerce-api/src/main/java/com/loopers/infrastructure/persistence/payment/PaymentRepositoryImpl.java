package com.loopers.infrastructure.persistence.payment;

import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.payment.PaymentStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByOrderNumber(OrderNumber orderNumber) {
        return paymentJpaRepository.findByOrderNumber(orderNumber.number());
    }

    @Override
    public List<Payment> findAllByStatus(PaymentStatus status) {
        return paymentJpaRepository.findAllByStatus(status);
    }
}
