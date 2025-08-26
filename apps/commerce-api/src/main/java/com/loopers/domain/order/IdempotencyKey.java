package com.loopers.domain.order;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record IdempotencyKey(String key) {

    public static IdempotencyKey generate() {
        return new IdempotencyKey(UUID.randomUUID().toString());
    }

    public static IdempotencyKey of(String idempotencyKey) {
        return new IdempotencyKey(idempotencyKey);
    }
}
