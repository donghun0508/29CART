package com.loopers.logging.support.alert;

import static com.loopers.logging.config.LoggingConstant.LOGGING;
import static com.loopers.logging.support.NotificationChannel.SLACK;

import com.loopers.logging.support.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class NotificationEventListener {

    private final NotificationSender notificationSender;

    @Async(LOGGING)
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener(NotificationEvent.class)
    public void handle(NotificationEvent event) {
        notificationSender.send(SLACK, event.message());
    }
}
