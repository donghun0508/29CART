package com.loopers.domain.command.order;


import static com.loopers.utils.ValidationUtils.requireNonEmpty;
import static com.loopers.utils.ValidationUtils.requireNonNull;
import static com.loopers.utils.ValidationUtils.requirePositive;

import java.util.List;
import lombok.Builder;

public class OrderCommand {

    public record Create(
        Long userId,
        List<OrderItem> items
    ) {

        public Create {
            requireNonNull(userId, "회원 Id는 null일 수 없습니다.");
            requireNonNull(items, "주문 항목은 null일 수 없습니다.");
            requireNonEmpty(items, "주문 항목은 비어있을 수 없습니다.");
        }

        public List<Long> productIds() {
            return items.stream()
                .map(OrderItem::productId)
                .toList();
        }

        public Long totalPrice() {
            return items.stream()
                .mapToLong(item -> item.price() * item.quantity())
                .sum();
        }

        @Builder
        public record OrderItem(Long productId, Long price, Long quantity) {

            public OrderItem {
                requirePositive(productId, "상품 ID는 null 또는 0일 수 없습니다.");
                requirePositive(price, "상품 가격은 null 또는 0일 수 없습니다.");
                requirePositive(quantity, "상품 수량은 null 또는 0일 수 없습니다.");
            }

            public Long totalPrice() {
                return price * quantity;
            }
        }
    }
}
