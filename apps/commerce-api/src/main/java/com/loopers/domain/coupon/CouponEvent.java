package com.loopers.domain.coupon;

import com.loopers.domain.shared.DomainEvent;
import com.loopers.logging.support.alert.NotificationEvent;

public class CouponEvent {

    public static class CouponUsedEvent extends DomainEvent implements NotificationEvent {
        private final Long issuedCouponId;
        private final DiscountType discountType;
        private final Long disCountValue;

        public CouponUsedEvent(IssuedCoupon issuedCoupon) {
            this.issuedCouponId = issuedCoupon.getId();
            this.discountType = issuedCoupon.getDiscountType();
            this.disCountValue = issuedCoupon.getDiscountValue();
        }

        @Override
        public String message() {
            return String.format("쿠폰이 사용되었습니다. [쿠폰ID: %d] [할인타입: %s] [할인값: %d]", issuedCouponId, discountType, disCountValue);
        }
    }

    public static class CouponCancelledEvent extends DomainEvent implements NotificationEvent {
        private final Long issuedCouponId;
        private final DiscountType discountType;
        private final Long disCountValue;

        public CouponCancelledEvent(IssuedCoupon issuedCoupon) {
            this.issuedCouponId = issuedCoupon.getId();
            this.discountType = issuedCoupon.getDiscountType();
            this.disCountValue = issuedCoupon.getDiscountValue();
        }

        @Override
        public String message() {
            return String.format("쿠폰이 취소되었습니다. [쿠폰ID: %d] [할인타입: %s] [할인값: %d]", issuedCouponId, discountType, disCountValue);
        }
    }
}
