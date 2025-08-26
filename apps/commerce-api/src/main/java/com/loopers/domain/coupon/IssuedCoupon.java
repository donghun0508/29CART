package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "issued_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuedCoupon extends BaseEntity {

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code;

    @AttributeOverrides({
        @AttributeOverride(name = "targetScope", column = @Column(name = "target_scope", nullable = false, updatable = false)),
        @AttributeOverride(name = "targetId", column = @Column(name = "target_id", nullable = false, updatable = false)),
        @AttributeOverride(name = "issuedAt", column = @Column(name = "issued_at", nullable = false, updatable = false)),
        @AttributeOverride(name = "expiredAt", column = @Column(name = "expired_at", updatable = false))
    })
    private Issuance issuance;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, updatable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false, updatable = false)
    private Long discountValue;

    @AttributeOverrides({
        @AttributeOverride(name = "status", column = @Column(name = "status", nullable = false)),
        @AttributeOverride(name = "usedAt", column = @Column(name = "used_at"))
    })
    private CouponState couponState;

    public Money use(Long targetId, Money orderAmount) {
        this.issuance.validate(targetId);
        this.couponState = this.couponState.used();
        return this.discountType.calculate(orderAmount, discountValue);
    }

    public void cancel() {
        this.couponState = this.couponState.canceled();
    }

    public boolean isUsed() {
        return this.couponState.isUsed();
    }

    public Long getTargetId() {
        return this.issuance.targetId();
    }
}
