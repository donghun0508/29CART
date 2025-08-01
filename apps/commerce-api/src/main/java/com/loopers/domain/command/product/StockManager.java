package com.loopers.domain.command.product;

import static com.loopers.utils.ValidationUtils.requireNonEmpty;

import java.util.List;
import java.util.Map;

public class StockManager {

    public void deduct(Map<Long, Long> orderItems, List<Product> products) {
        requireNonEmpty(orderItems, "주문 항목은 비어있을 수 없습니다.");
        requireNonEmpty(products, "상품 목록은 비어있을 수 없습니다.");

        for (Product product : products) {
            Long quantity = orderItems.get(product.getId());
            if (quantity != null && quantity > 0) {
                product.decreaseStock(quantity);
            }
        }
    }
}
