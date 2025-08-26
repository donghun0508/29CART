package com.loopers.domain.payment;

import com.loopers.domain.order.OrderNumber;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment findByOrderNumber(OrderNumber orderNumber) {
        return paymentRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new IllegalArgumentException("결제가 진행되지 않았습니다. 주문번호: " + orderNumber.number()));
    }

    @Transactional
    public List<Payment> findAllByStatus(PaymentStatus status) {
        return paymentRepository.findAllByStatus(status);
    }
}
