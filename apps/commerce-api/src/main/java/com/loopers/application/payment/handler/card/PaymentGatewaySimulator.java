package com.loopers.application.payment.handler.card;


import static com.loopers.domain.payment.PaymentProvider.SIMULATOR;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.data.CardMethod;
import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.CardPayment;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentClient;
import com.loopers.domain.payment.PaymentClientData.PaymentClientRequest;
import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import com.loopers.domain.payment.PaymentProvider;
import com.loopers.domain.payment.SupportPg.PgSimulator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class PaymentGatewaySimulator implements PaymentGateway {

    @Value("${external.payment.providers.simulator.callback-url}")
    private String callBackUrl;

    private final PaymentClient paymentClient;
    private final OrderService orderService;
    private final IssuedCouponService issuedCouponService;

    public PaymentGatewaySimulator(@PgSimulator PaymentClient paymentClient, OrderService orderService,
        IssuedCouponService issuedCouponService) {
        this.paymentClient = paymentClient;
        this.orderService = orderService;
        this.issuedCouponService = issuedCouponService;
    }

    @Override
    public boolean supports(PaymentProvider selectedPg) {
        return selectedPg == SIMULATOR;
    }

    @Override
    public Payment processPayment(PaymentRequestCommand command) {
        Order order = orderService.findByOrderNumber(command.orderNumber());
        CardMethod cardData = (CardMethod) command.paymentMethod();

        // 결제 요청
        PaymentClientRequest paymentClientRequest = new PaymentClientRequest(order.getOrderNumber().number(), cardData.cardType(),
            cardData.cardNumber().number(), order.paidAmount().value(), callBackUrl);
        PaymentClientResponse paymentClientResponse = paymentClient.requestPayment(paymentClientRequest);

        if (paymentClientResponse.isFail()) {
            // 주문 취소
            order.fail();

            // 쿠폰 취소
            if (order.isCouponUsed()) {
                IssuedCoupon issuedCoupon = issuedCouponService.findById(order.getCouponId());
                issuedCoupon.cancel();
            }
            return CardPayment.failCard(order.getOrderNumber().number(), order.paidAmount(), cardData.cardType(), SIMULATOR,
                paymentClientResponse);
        } else {
            return CardPayment.initiate(order.getOrderNumber().number(), order.paidAmount(), cardData.cardType(), SIMULATOR,
                paymentClientResponse, paymentClientResponse.transactionKey());
        }
    }
}
