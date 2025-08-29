package com.loopers.application.payment.processor;

import com.loopers.application.payment.processor.PointPaymentProcessor.PointPaymentRequest;
import com.loopers.application.payment.processor.support.PaymentExceptionTranslator;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.PointPayment;
import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.shared.Money;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class PointPaymentProcessor extends PaymentProcessorTemplate<PointPaymentRequest, PointPayment> {

    private final UserService userService;

    PointPaymentProcessor(
        OrderService orderService,
        PaymentService paymentService,
        DomainEventPublisher domainEventPublisher,
        PaymentExceptionTranslator paymentExceptionTranslator,
        UserService userService
    ) {
        super(orderService, paymentService, domainEventPublisher, paymentExceptionTranslator);
        this.userService = userService;
    }

    @Override
    protected PointPayment doCreate(PointPaymentRequest command) {
        return PointPayment.create(command.orderNumber().number(), command.paidAmount(), command.buyerId());
    }

    @Override
    protected void doExecute(PointPayment pointPayment, PointPaymentRequest command) {
        User user = userService.findById(command.buyerId());
        user.payWithPoints(pointPayment.getPaidAmount());
        pointPayment.complete();
    }

    public record PointPaymentRequest(OrderNumber orderNumber, Long buyerId, Money paidAmount) implements PaymentRequest {

    }
}
