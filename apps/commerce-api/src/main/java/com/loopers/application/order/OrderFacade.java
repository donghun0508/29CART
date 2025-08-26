package com.loopers.application.order;

import com.loopers.application.order.OrderCommand.OrderRequestCommand;
import com.loopers.domain.catalog.Inventory;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderEvent.OrderCreatedEvent;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderItemConverter orderItemConverter;
    private final IssuedCouponService issuedCouponService;

    @Transactional
    public OrderCreatedEvent createOrder(OrderRequestCommand command) {
        orderService.findByIdempotencyKeyOptional(command.idempotencyKey())
            .ifPresent(order -> {
                throw new IllegalStateException("이미 주문이 존재합니다. 주문 번호: " + order.getOrderNumber().number());
            });

        User buyer = userService.findByAccountId(command.accountId());
        List<Product> actualProducts = productService.findAll(command.purchaseProductIds());

        // 재고 확인
        Inventory inventory = Inventory.allocate(actualProducts, command.purchaseProducts());
        inventory.check();

        // 주문 생성
        Order order = orderService.create(Order.from(buyer.getId(), command.idempotencyKey(), orderItemConverter.convert(inventory.items())));

        // 쿠폰 사용
        command.findCoupon().map(issuedCouponService::findByIdWithLock).ifPresent(order::applyCoupon);
        return new OrderCreatedEvent(order);
    }
}
