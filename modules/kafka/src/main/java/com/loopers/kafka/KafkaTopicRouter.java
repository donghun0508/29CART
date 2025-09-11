package com.loopers.kafka;

import org.springframework.stereotype.Component;

@Component
class KafkaTopicRouter {

    public String getTopic(String type) {
        if (type == null || type.isBlank()) {
            return "domain-events";
        }

        if (type.contains("Payment")) {
            return "payment-events";
        }

        if(type.contains("Product")) {
            return "catalog-events";
        }

        String prefix = type.replaceAll("([A-Z][a-zA-Z0-9]+?)([A-Z].*)", "$1");
        return prefix.toLowerCase() + "-events";
    }
}
