package com.loopers.domain.payment;


import static com.loopers.domain.payment.PaymentStatus.PENDING;

import com.loopers.domain.payment.PaymentEvent.PointPaymentCreatedEvent;
import com.loopers.domain.shared.Money;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_payment")
@DiscriminatorValue("POINT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointPayment extends Payment {

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "point_balance", nullable = false, updatable = false)
    private Money beforePointBalance;

    public static PointPayment initiate(String orderNumber, Money paidAmount, Long userId, Money beforePointBalance) {
        PointPayment pointPayment = new PointPayment();

        pointPayment.orderNumber = orderNumber;
        pointPayment.paidAmount = paidAmount;
        pointPayment.status = PENDING;
        pointPayment.userId = userId;
        pointPayment.beforePointBalance = beforePointBalance;
        pointPayment.registerEvent(new PointPaymentCreatedEvent(pointPayment));

        return pointPayment;
    }

}
