package com.loopers.infrastructure.persistence.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderNumber(String orderNumber);

    @Query("SELECT p FROM Payment p WHERE TYPE(p) = CardPayment AND p.status = :status")
    List<Payment> findAllByStatus(@Param("status") PaymentStatus status);
}
