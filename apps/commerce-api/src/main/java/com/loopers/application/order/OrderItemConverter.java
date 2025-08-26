package com.loopers.application.order;

import com.loopers.domain.order.OrderItem;
import com.loopers.domain.shared.ProductItem;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class OrderItemConverter implements Converter<ProductItem, OrderItem> {

    public List<OrderItem> convert(List<ProductItem> items) {
        return items.stream().map(this::convert).toList();
    }

    @Override
    public OrderItem convert(ProductItem source) {
        return new OrderItem(
            source.productId(),
            source.unitPrice(),
            source.quantity()
        );
    }
}
