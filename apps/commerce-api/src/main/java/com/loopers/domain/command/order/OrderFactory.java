package com.loopers.domain.command.order;

import com.loopers.domain.command.order.OrderCommand.Create;
import com.loopers.domain.command.order.OrderCommand.Create.OrderItem;
import com.loopers.domain.command.product.Product;
import com.loopers.domain.command.user.User;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderFactory {

    public Order createOrder(User user, List<Product> products, OrderForm orderForm) {
        Map<Long, Long> orderItems = orderForm.getPurchaseItemQuantityMap();
        Map<Long, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<OrderItem> orderItemList = orderItems.entrySet().stream()
            .map(entry -> {
                Long productId = entry.getKey();
                Long quantity = entry.getValue();

                Product product = productMap.get(productId);
                if (product == null) {
                    throw new IllegalArgumentException("존재하지 않는 상품입니다. productId: " + productId);
                }

                return OrderItem.builder()
                    .productId(productId)
                    .price(product.getPrice().value())
                    .quantity(quantity)
                    .build();
            })
            .toList();

        Create command = new Create(user.getId(), orderItemList);

        return Order.create(command);
    }

}
