package com.loopers.application.order;

import com.loopers.domain.command.order.Order;
import com.loopers.domain.command.order.OrderFactory;
import com.loopers.domain.command.order.OrderForm;
import com.loopers.domain.command.order.OrderService;
import com.loopers.domain.command.product.Product;
import com.loopers.domain.command.product.ProductService;
import com.loopers.domain.command.product.StockManager;
import com.loopers.domain.command.user.User;
import com.loopers.domain.command.user.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final StockManager stockManager;
    private final OrderFactory orderFactory;

    @Transactional
    public void requestOrder(String userId, OrderForm orderForm) {
        User buyer = userService.findByUserId(userId);
        List<Product> products = productService.findAllById(orderForm.productIds());

        Order order = orderFactory.createOrder(buyer, products, orderForm);
        stockManager.deduct(order.lines(), products);

        buyer.usePoints(order.getTotalAmount());

        userService.save(buyer);
        orderService.save(order);
        productService.saveAll(products);
    }
}
