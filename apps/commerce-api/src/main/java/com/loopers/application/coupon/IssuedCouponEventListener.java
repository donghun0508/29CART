package com.loopers.application.coupon;

import static com.loopers.async.config.AsyncConstant.DEFAULT;

import com.loopers.domain.order.OrderEvent.OrderAppliedCouponEvent;
import com.loopers.domain.order.OrderEvent.OrderFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
class IssuedCouponEventListener {

    private final IssuedCouponFacade issuedCouponFacade;

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderAppliedCouponEvent e) {
        issuedCouponFacade.use(e.getIssuedCouponId());
    }

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderFailedEvent orderFailedEvent) {
        if(orderFailedEvent.isCouponUsed()) {
            issuedCouponFacade.cancel(orderFailedEvent.getIssuedCouponId());
        }
    }
}
