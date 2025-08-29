package com.loopers.domain.coupon;

import static com.loopers.domain.shared.Money.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.shared.Money;
import com.loopers.fixture.MoneyFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@UnitTest
class FixedDiscountPolicyTest {

    @DisplayName("고정 할인 금액 계산 시, 원가가 null 일 경우 예외가 발생한다.")
    @Test
    void throwsExceptionWhenOriginalPriceIsNull() {
        FixedDiscountPolicy discountPolicy = new FixedDiscountPolicy();

        assertThatThrownBy(() -> discountPolicy.calculateDiscount(null, 1000L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("FixedDiscountPolicy.calculateDiscount().originalPrice");
    }

    @DisplayName("고정 할인 금액 계산 시, 할인 금액이 음수일 경우 예외가 발생한다.")
    @Test
    void throwsExceptionWhenDiscountValueIsNegative() {
        FixedDiscountPolicy discountPolicy = new FixedDiscountPolicy();

        assertThatThrownBy(() -> discountPolicy.calculateDiscount(Money.of(5000L), -1000L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("FixedDiscountPolicy.calculateDiscount().discountValue");
    }

    @DisplayName("고정 할인 금액 계산 시, 원가가 할인 금액보다 작거나 같은 경우 0원을 반환한다.")
    @Test
    void returnsZeroWhenOriginalPriceIsLessThanOrEqualToDiscountValue() {
        FixedDiscountPolicy discountPolicy = new FixedDiscountPolicy();
        Money originalPrice = MoneyFixture.builder().build();
        Long discountValue = originalPrice.value() + 1L;

        Money discountedPrice = discountPolicy.calculateDiscount(originalPrice, discountValue);
        assertThat(discountedPrice).isEqualTo(Money.ZERO);
    }

    @DisplayName("고정 할인 금액 계산 시, 정상적인 경우 원가에서 할인 금액을 뺀 값을 반환한다.")
    @Test
    void returnsDiscountedPriceWhenValidInputs() {
        final Long originalAmount = 5000L;
        final Long discountAmount = 1000L;

        FixedDiscountPolicy discountPolicy = new FixedDiscountPolicy();
        Money originalPrice = MoneyFixture.builder().amount(originalAmount).build();

        Money discountedPrice = discountPolicy.calculateDiscount(originalPrice, discountAmount);
        assertThat(discountedPrice).isEqualTo(Money.of(originalAmount).subtract(Money.of(discountAmount)));
    }



}
