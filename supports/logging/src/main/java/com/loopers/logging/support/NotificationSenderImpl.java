package com.loopers.logging.support;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
class NotificationSenderImpl implements NotificationSender {

    private final Map<NotificationChannel, NotificationGateway> gatewayMap;

    public NotificationSenderImpl(List<NotificationGateway> gateways) {
        this.gatewayMap = gateways.stream()
            .collect(Collectors.toMap(NotificationGateway::channel, Function.identity()));
    }

    @Override
    public void send(NotificationChannel notificationChannel, String message) {
        NotificationGateway gateway = gatewayMap.get(notificationChannel);
        if (gateway != null) {
            gateway.send(message);
        }
    }
}
