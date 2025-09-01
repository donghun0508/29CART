package com.loopers.logging.notification.slack;

import com.loopers.logging.support.NotificationChannel;
import com.loopers.logging.support.NotificationGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class SlackNotificationGateway implements NotificationGateway {

    private static final Logger logger = LoggerFactory.getLogger(SlackNotificationGateway.class);

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.SLACK;
    }

    @Override
    public void send(String message) {
        logger.info("[slack] {}", message);
    }
}
