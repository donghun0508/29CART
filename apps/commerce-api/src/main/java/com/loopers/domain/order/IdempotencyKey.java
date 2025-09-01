package com.loopers.domain.order;

import static com.loopers.domain.shared.Preconditions.requireNonBlank;

import com.loopers.domain.shared.Preconditions;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record IdempotencyKey(String key) {

    public IdempotencyKey {
        requireNonBlank(key, "IdempotencyKey.key: Idempotency Key는 필수입니다.");
    }

    public static IdempotencyKey generate() {
        return new IdempotencyKey(UUID.randomUUID().toString());
    }

    public static IdempotencyKey of(String idempotencyKey) {
        return new IdempotencyKey(idempotencyKey);
    }
}
