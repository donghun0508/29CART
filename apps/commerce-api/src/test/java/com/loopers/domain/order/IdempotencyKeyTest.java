package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.loopers.config.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@UnitTest
class IdempotencyKeyTest {

    @DisplayName("멱등키는 null인 경우 예외가 발생한다.")
    @Test
    void throwsExceptionWhenKeyIsNull() {
        assertThatThrownBy(() -> IdempotencyKey.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("IdempotencyKey.key");
    }

    @DisplayName("멱등키가 자동으로 생성된다.")
    @Test
    void generatesIdempotencyKey() {
        IdempotencyKey idempotencyKey = IdempotencyKey.generate();

        assertNotNull(idempotencyKey.key());
    }

    @DisplayName("멱등키가 주어진 값으로 생성된다.")
    @Test
    void createsIdempotencyKeyFromGivenValue() {
        String key = "test-key";
        IdempotencyKey idempotencyKey = IdempotencyKey.of(key);

        assertEquals(key, idempotencyKey.key());
    }

}
