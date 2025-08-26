package com.loopers.domain.coupon;

import static com.loopers.domain.shared.Preconditions.requireNonNull;
import static com.loopers.domain.shared.Preconditions.requirePositive;

import com.loopers.domain.shared.Money;

class FixedDiscountPolicy implements DiscountPolicy {

    @Override
    public Money calculateDiscount(Money originalPrice, Long discountValue) {
        requireNonNull(originalPrice, "원가는 null일 수 없습니다.");
        requirePositive(discountValue, "할인 금액은 양수여야 합니다.");

        Money discountMoney = Money.of(discountValue);
        if (originalPrice.isLessThanOrEqual(discountMoney)) {
            return Money.ZERO;
        }
        return originalPrice.subtract(discountMoney);
    }
}
