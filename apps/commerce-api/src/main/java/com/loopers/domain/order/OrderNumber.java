package com.loopers.domain.order;

import static com.loopers.domain.shared.Preconditions.requireNonBlank;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record OrderNumber(String number) {

    private static final String PREFIX = "29CART-";

    public OrderNumber {
        requireNonBlank(number, "주문 번호는 비어있을 수 없습니다.");
    }

    public static OrderNumber generate() {
        String uuid = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 12)
            .toUpperCase();
        return new OrderNumber(PREFIX + uuid);
    }

    public static OrderNumber of(String number) {
        return new OrderNumber(number);
    }
}
