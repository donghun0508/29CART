package com.loopers.domain.shared;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderCoupon(Long issuedCouponId, Money paidPrice) {

    public OrderCoupon {
        requireNonNull(paidPrice, "결제 금액은 null일 수 없습니다");
    }

    public static OrderCoupon withCoupon(Long issuedCouponId, Money finalAmount) {
        return new OrderCoupon(issuedCouponId, finalAmount);
    }

    public static OrderCoupon newInstance(OrderCoupon orderCoupon) {
        return new OrderCoupon(orderCoupon.issuedCouponId, orderCoupon.paidPrice);
    }

    public boolean hasCoupon() {
        return issuedCouponId != null;
    }

    public static OrderCoupon empty(Money totalAmount) {
        return new OrderCoupon(null, totalAmount);
    }

    public Money calculate(Money totalAmount) {
        return totalAmount.subtract(this.paidPrice);
    }
}
