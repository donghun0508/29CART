package com.loopers.application.payment.handler.point;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.data.PaymentMethod;
import com.loopers.application.payment.data.PointMethod;
import com.loopers.application.payment.handler.PaymentHandler;
import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PointPayment;
import com.loopers.domain.shared.ExceptionMatcher;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class PointPaymentHandler implements PaymentHandler {

    private final OrderService orderService;
    private final UserService userService;
    private final ExceptionMatcher exceptionMatcher;
    private final IssuedCouponService issuedCouponService;

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return paymentMethod instanceof PointMethod;
    }

    @Override
    public Payment process(PaymentRequestCommand command) {
        Order order = orderService.findByOrderNumberWithLock(command.orderNumber());
        User user = userService.findByIdWithLock(order.getBuyerId());

        // 결제 생성
        PointPayment pointPayment = PointPayment.initiate(order.getOrderNumber().number(), order.paidAmount(), user.getId(),
            user.getTotalPoint());

        try {
            // 포인트 차감
            user.payWithPoints(order.paidAmount());

            // 결제 성공
            pointPayment.complete();

            // 주문 성공
            order.complete();
        } catch (Exception e) {
            if (exceptionMatcher.isNotEnoughPoint(e)) {
                pointPayment.fail("포인트가 부족합니다.");
            } else {
                log.error("포인트 결제 실패 :{} ", e.getMessage(), e);
                pointPayment.fail(e.getMessage());
            }
            order.fail();

            // 쿠폰 취소
            if (order.isCouponUsed()) {
                IssuedCoupon issuedCoupon = issuedCouponService.findById(order.getCouponId());
                issuedCoupon.cancel();
            }
        }

        return pointPayment;
    }
}
