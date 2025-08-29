package com.loopers.logging.notification.console;

import com.loopers.logging.support.NotificationChannel;
import com.loopers.logging.support.NotificationGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class ConsoleNotificationGateway implements NotificationGateway {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleNotificationGateway.class);

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.CONSOLE;
    }

    @Override
    public void send(String message) {
        logger.info("[console] {}", message);
    }
}
