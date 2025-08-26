package com.loopers.domain.order;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

import com.loopers.domain.shared.Money;

public record OrderItem(Long productId, Money price, Quantity quantity) {

    public OrderItem {
        requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(price, "가격은 null일 수 없습니다.");
        requireNonNull(quantity, "수량은 null일 수 없습니다.");
    }

    public OrderItem(Long productId, Long price, Long quantity) {
        this(
            requireNonNull(productId, "상품 ID는 null일 수 없습니다."),
            Money.of(requireNonNull(price, "가격은 null일 수 없습니다.")),
            Quantity.of(requireNonNull(quantity, "수량은 null일 수 없습니다."))
        );
    }

    public static OrderItem of(Long productId, Long price, Long quantity) {
        return new OrderItem(productId, Money.of(price), Quantity.of(quantity));
    }

    public Money totalPrice() {
        return price.multiply(quantity.count());
    }
}
