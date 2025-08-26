package com.loopers.application.order;

import com.loopers.application.order.OrderCommand.OrderRequestCommand;
import com.loopers.application.order.OrderResult.OrderPaymentResult;
import com.loopers.application.payment.PaymentCommand.PaymentRequestCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.domain.order.OrderEvent.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderPaymentFacade {

    private final OrderFacade orderFacade;
    private final PaymentFacade paymentFacade;

    public OrderPaymentResult order(OrderRequestCommand command) {
        OrderCreatedEvent orderCreatedEvent = orderFacade.createOrder(command);
        paymentFacade.paymentRequest(new PaymentRequestCommand(orderCreatedEvent.orderNumber(), command.paymentMethod()));
        return new OrderPaymentResult(orderCreatedEvent.orderNumber());
    }
}
