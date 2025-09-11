package com.loopers.domain.coupon;


import com.loopers.common.domain.DomainEvent;
import lombok.Getter;

public class CouponEvent {

    @Getter
    public static class CouponUsedEvent implements DomainEvent {

        private final Long issuedCouponId;
        private final DiscountType discountType;
        private final Long disCountValue;

        public CouponUsedEvent(IssuedCoupon issuedCoupon) {
            this.issuedCouponId = issuedCoupon.getId();
            this.discountType = issuedCoupon.getDiscountType();
            this.disCountValue = issuedCoupon.getDiscountValue();
        }
    }

    @Getter
    public static class CouponCancelledEvent implements DomainEvent {

        private final Long issuedCouponId;
        private final DiscountType discountType;
        private final Long disCountValue;

        public CouponCancelledEvent(IssuedCoupon issuedCoupon) {
            this.issuedCouponId = issuedCoupon.getId();
            this.discountType = issuedCoupon.getDiscountType();
            this.disCountValue = issuedCoupon.getDiscountValue();
        }
    }
}
