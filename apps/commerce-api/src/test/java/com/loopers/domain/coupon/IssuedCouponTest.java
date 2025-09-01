package com.loopers.domain.coupon;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.shared.Money;
import com.loopers.domain.shared.OrderCoupon;
import com.loopers.fixture.IssuedCouponFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@UnitTest
class IssuedCouponTest {

    @DisplayName("회원 쿠폰 사용 시, 다른 사용자 ID로 사용하려고 할 때 예외를 발생시킨다.")
    @Test
    void throwsExceptionWhenUsingCouponWithDifferentUserId() {
        CouponState couponState = new CouponState(CouponStatus.AVAILABLE, null);
        IssuedCoupon issuedCoupon = IssuedCouponFixture.builder().couponState(couponState).build();

        assertThatThrownBy(() -> issuedCoupon.calculate(issuedCoupon.getTargetId() + 1, Money.of(5000L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Issuance.validate().targetId");
    }

    @DisplayName("회원 쿠폰 사용 시, 쿠폰이 만료된 경우 예외를 발생시킨다.")
    @Test
    void throwsExceptionWhenCouponIsExpired() {
        CouponState couponState = new CouponState(CouponStatus.AVAILABLE, null);
        IssuedCoupon issuedCoupon = IssuedCouponFixture.builder().expiredAt(ZonedDateTime.now().minusDays(1)).couponState(couponState).build();

        assertThatThrownBy(() -> issuedCoupon.calculate(issuedCoupon.getTargetId(), Money.of(5000L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Issuance.validate().expiredAt");
    }

    @DisplayName("회원 쿠폰 사용 시, 이미 사용된 쿠폰인 경우 예외를 발생시킨다.")
    @Test
    void throwsExceptionWhenCouponAlreadyUsed() {
        CouponState couponState = new CouponState(CouponStatus.USED, LocalDateTime.now());
        IssuedCoupon issuedCoupon = IssuedCouponFixture.builder().couponState(couponState).build();

        assertThatThrownBy(issuedCoupon::use)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CouponState.used().status");
    }

    @DisplayName("회원 쿠폰 사용 시, 쿠폰이 사용 가능한 상태가 아닐 때 예외를 발생시킨다.")
    @Test
    void throwsExceptionWhenCouponIsNotAvailable() {
        CouponState couponState = new CouponState(CouponStatus.INVALID, null);
        IssuedCoupon issuedCoupon = IssuedCouponFixture.builder().couponState(couponState).build();

        assertThatThrownBy(issuedCoupon::use)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CouponState.used().status");
    }

    @DisplayName("회원 쿠폰 사용 시, 정액 할인 쿠폰이 유효한 경우 정상적으로 사용된다.")
    @Test
    void usesCouponWhenValid() {
        Long discountValue = 1000L;
        Money orderAmount = Money.of(5000L);
        CouponState couponState = new CouponState(CouponStatus.AVAILABLE, null);
        IssuedCoupon issuedCoupon = IssuedCouponFixture.builder().couponState(couponState).discountType(DiscountType.FIXED).discountValue(discountValue).build();

        OrderCoupon orderCoupon = issuedCoupon.calculate(issuedCoupon.getTargetId(), orderAmount);

        assertThat(orderCoupon.paidPrice().value()).isEqualTo(orderAmount.value() - discountValue);
    }

    @DisplayName("회원 쿠폰 사용 시, 정률 할인 쿠폰이 유효한 경우 정상적으로 사용된다.")
    @Test
    void usesPercentDiscountCouponWhenValid() {
        Long discountValue = 20L;
        Money orderAmount = Money.of(5000L);
        CouponState couponState = new CouponState(CouponStatus.AVAILABLE, null);
        IssuedCoupon issuedCoupon = IssuedCouponFixture.builder().couponState(couponState).discountType(DiscountType.PERCENT).discountValue(discountValue).build();

        OrderCoupon orderCoupon = issuedCoupon.calculate(issuedCoupon.getTargetId(), orderAmount);

        assertThat(orderCoupon.paidPrice().value()).isEqualTo(orderAmount.value() - (orderAmount.value() * discountValue / 100));
    }


}
