package com.loopers.logging.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LogNotificationPort implements NotificationPort {

    private static final Logger logger = LoggerFactory.getLogger(LogNotificationPort.class);

    @Override
    public void send(String message) {
        logger.info(message);
    }
}
