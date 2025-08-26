package com.loopers.domain.payment;

import com.loopers.domain.shared.AggregateRoot;
import com.loopers.domain.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "payment")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Payment extends AggregateRoot {

    @Column(
        name = "order_number",
        nullable = false,
        updatable = false,
        unique = true
    )
    protected String orderNumber;

    @AttributeOverride(name = "value", column = @Column(name = "paid_amount", nullable = false, updatable = false))
    protected Money paidAmount;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false
    )
    protected PaymentStatus status;

    @Column(name = "failure_reason", nullable = true)
    private String failureReason;

    public void complete() {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.REQUESTED) {
            throw new IllegalStateException("결제 진행 중인 상태가 아닙니다.");
        }
        this.status = PaymentStatus.COMPLETED;
    }

    public void fail(String reason) {
        if (this.status == PaymentStatus.FAILED) {
            throw new IllegalStateException("이미 결제 실패 상태입니다.");
        }
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    public boolean isFailed() {
        return this.status == PaymentStatus.FAILED;
    }

    public String failureReason() {
        return this.failureReason;
    }
}
