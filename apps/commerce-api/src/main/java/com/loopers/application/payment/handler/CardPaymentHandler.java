package com.loopers.application.payment.handler;

import static com.loopers.domain.payment.PaymentProvider.SIMULATOR;

import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.data.CardMethod;
import com.loopers.application.payment.data.PaymentMethod;
import com.loopers.domain.catalog.Inventory;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.CardPayment;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentClient;
import com.loopers.domain.payment.PaymentClientData.PaymentClientRequest;
import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class CardPaymentHandler implements PaymentHandler {

    @Value("${external.payment.providers.simulator.callback-url}")
    private String callBackUrl;

    private final PaymentClient paymentClient;
    private final OrderService orderService;
    private final ProductService productService;
    private final IssuedCouponService issuedCouponService;

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return paymentMethod instanceof CardMethod;
    }

    @Override
    public Payment process(PaymentRequestCommand command) {
        Order order = orderService.findByOrderNumber(command.orderNumber());
        CardMethod cardData = (CardMethod) command.paymentMethod();
        List<Product> products = productService.findAllWithLock(order.purchaseProductIds());

        Map<Long, Long> stockMap = order.purchaseProducts();
        Inventory inventory = Inventory.convert(products, stockMap);

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
            // 주문 성공
            order.complete();

            // 재고 차감
            inventory.decreaseStock();
            return CardPayment.initiate(order.getOrderNumber().number(), order.paidAmount(), cardData.cardType(), SIMULATOR,
                paymentClientResponse, paymentClientResponse.transactionKey());
        }
    }
}
