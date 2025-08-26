package com.loopers.domain.shared;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.Stock;

public record ProductItem(Long productId, Long unitPrice, Long quantity) {

    public Money totalPrice() {
        return Money.of(unitPrice).multiply(quantity);
    }

    public static ProductItem of(Product product, Stock quantity) {
        return new ProductItem(
            product.getId(),
            product.getUnitPrice().value(),
            quantity.count()
        );
    }
}
