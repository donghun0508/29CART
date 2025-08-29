package com.loopers.application.payment.processor;

import static com.loopers.domain.payment.PaymentProvider.SIMULATOR;

import com.loopers.application.payment.processor.CardPaymentProcessor.CardPaymentRequest;
import com.loopers.application.payment.processor.support.PaymentExceptionTranslator;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.CardPayment;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.shared.Money;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentProcessor extends PaymentProcessorTemplate<CardPaymentRequest, CardPayment> {

    protected CardPaymentProcessor(
        OrderService orderService,
        PaymentService paymentService,
        DomainEventPublisher domainEventPublisher,
        PaymentExceptionTranslator paymentExceptionTranslator
    ) {
        super(orderService, paymentService, domainEventPublisher, paymentExceptionTranslator);
    }

    @Override
    protected CardPayment doCreate(CardPaymentRequest command) {
        return CardPayment.create(command.orderNumber().number(), command.paidAmount(), command.cardType(), SIMULATOR);
    }

    @Override
    protected void doExecute(CardPayment cardPayment, CardPaymentRequest command) {
        PaymentClientResponse paymentClientResponse = command.paymentClientResponse();
        if (paymentClientResponse.isFail()) {
            throw new PaymentException(paymentClientResponse.reason());
        }
        cardPayment.request();
    }

    public record CardPaymentRequest(OrderNumber orderNumber, Money paidAmount, CardType cardType,
                                     PaymentClientResponse paymentClientResponse) implements PaymentRequest {

    }
}
