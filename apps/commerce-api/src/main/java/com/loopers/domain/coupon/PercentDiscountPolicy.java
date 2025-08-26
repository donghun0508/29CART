package com.loopers.domain.coupon;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

import com.loopers.domain.shared.Money;

class PercentDiscountPolicy implements DiscountPolicy {

    @Override
    public Money calculateDiscount(Money originalPrice, Long discountValue) {
        requireNonNull(originalPrice, "원가는 null일 수 없습니다.");
        requireNonNull(discountValue, "할인율은 null일 수 없습니다.");

        if (discountValue < 0 || discountValue > 100) {
            throw new IllegalArgumentException("할인율은 0~100% 사이여야 합니다.");
        }

        long originalAmount = originalPrice.value();
        long discountAmount = originalAmount * discountValue / 100;

        return Money.of(originalAmount - discountAmount);
    }
}
