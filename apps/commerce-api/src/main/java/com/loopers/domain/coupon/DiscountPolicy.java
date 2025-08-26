package com.loopers.domain.coupon;

import com.loopers.domain.shared.Money;

@FunctionalInterface
public interface DiscountPolicy {

    Money calculateDiscount(Money originalPrice, Long discountValue);
}
