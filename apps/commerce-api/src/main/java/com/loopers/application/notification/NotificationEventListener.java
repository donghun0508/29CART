package com.loopers.application.notification;

import static com.loopers.async.config.AsyncConstant.DEFAULT;

import com.loopers.logging.notification.NotificationSender;
import com.loopers.logging.notification.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
class NotificationEventListener {

    private final NotificationSender notificationSender;

    @Async(DEFAULT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(NotificationEvent event) {
        notificationSender.send(event.message());
    }
}
