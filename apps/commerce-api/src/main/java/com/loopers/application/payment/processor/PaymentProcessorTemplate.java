package com.loopers.application.payment.processor;

import com.loopers.application.payment.processor.PaymentProcessor.PaymentRequest;
import com.loopers.application.payment.processor.support.PaymentExceptionTranslator;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.event.outbox.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class PaymentProcessorTemplate<T extends PaymentRequest, P extends Payment>
    implements PaymentProcessor<T> {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final EventStore eventStore;
    protected final PaymentExceptionTranslator paymentExceptionTranslator;

    @Override
    @Transactional
    public void execute(T command) {
        acquireOrderLock(command.orderNumber());
        P payment = doCreate(command);

        try {
            doExecute(payment, command);
        } catch (Exception e) {
            PaymentException paymentException = paymentExceptionTranslator.translate(e);
            payment.fail(paymentException.getReason());
        } finally {
            save(payment);
            eventStore.save(payment);
        }
    }

    protected abstract P doCreate(T command);

    protected abstract void doExecute(P payment, T command);

    private void acquireOrderLock(OrderNumber orderNumber) {
        orderService.findByOrderNumberWithLock(orderNumber);
    }

    private void save(P payment) {
        paymentService.save(payment);
    }
}
