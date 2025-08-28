package com.loopers.application.order;

import com.loopers.application.order.OrderCommand.OrderRequestCommand;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.catalog.Products;
import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.shared.OrderCoupon;
import com.loopers.domain.shared.StockReservations;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderFacade {

    private final UserService userService;
    private final IssuedCouponService issuedCouponService;
    private final ProductService productService;
    private final OrderService orderService;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public void place(OrderRequestCommand command) {
        User buyer = userService.findByAccountId(command.accountId());
        Products products = productService.findAllCollection(command.productIds());

        // 선 재고 차감
        StockReservations reservations = products.reserve(command.purchaseProducts());

        // 쿠폰 금액 계산
        OrderCoupon orderCoupon = issuedCouponService.findByIdOptional(command.couponId())
            .map(coupon -> coupon.calculate(buyer.getId(), reservations.totalPrice()))
            .orElse(OrderCoupon.empty(reservations.totalPrice()));

        // 주문 생성
        Order order = Order.create(buyer.getId(), command.idempotencyKey(), orderCoupon, reservations.getStockReservations(), command.paymentMethod());
        orderService.save(order);

        // 이벤트 발행
        domainEventPublisher.publishEvent(order.events());
    }

    @Transactional
    public void complete(OrderNumber orderNumber) {
        Order order = orderService.findByOrderNumber(orderNumber);
        order.complete();
        domainEventPublisher.publishEvent(order.events());
    }

    @Transactional
    public void fail(OrderNumber orderNumber) {
        Order order = orderService.findByOrderNumber(orderNumber);
        order.fail();
        order.hasCoupon()
            .map(orderCoupon -> issuedCouponService.findById(orderCoupon.issuedCouponId()))
            .ifPresent(IssuedCoupon::cancel);
        domainEventPublisher.publishEvent(order.events());
    }
}
