package com.loopers.domain.coupon;


import com.loopers.domain.shared.Money;

public enum DiscountType {
    FIXED(new FixedDiscountPolicy()),
    PERCENT(new PercentDiscountPolicy());

    private final DiscountPolicy calculator;

    DiscountType(DiscountPolicy calculator) {
        this.calculator = calculator;
    }

    public Money calculate(Money orderAmount, Long discountValue) {
        return calculator.calculateDiscount(orderAmount, discountValue);
    }
}
