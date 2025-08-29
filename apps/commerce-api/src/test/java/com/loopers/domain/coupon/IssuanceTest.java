package com.loopers.domain.coupon;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.loopers.config.annotations.UnitTest;
import com.loopers.fixture.IssuanceFixture;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@UnitTest
class IssuanceTest {

    @DisplayName("쿠폰 소유자 검증 시 대상 ID가 다르면 예외를 발생시킨다.")
    @Test
    void throwsExceptionWhenTargetIdIsDifferent() {
        final Long targetId = 1L;
        final Long differentTargetId = 2L;
        Issuance issuance = IssuanceFixture.builder().targetId(targetId).build();

        assertThatThrownBy(() -> issuance.validate(differentTargetId))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Issuance.validate().targetId");
    }

    @DisplayName("쿠폰 만료 검증 시 쿠폰이 만료되었으면 예외를 발생시킨다.")
    @Test
    void throwsExceptionWhenCouponIsExpired() {
        Issuance issuance = IssuanceFixture.builder().expiredAt(ZonedDateTime.now().minusDays(1)).build();

        assertThatThrownBy(() -> issuance.validate(issuance.targetId()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Issuance.validate().expiredAt");
    }

    @DisplayName("쿠폰 검증 시 대상 ID가 맞고 쿠폰이 만료되지 않았으면 예외가 발생하지 않는다.")
    @Test
    void doesNotThrowExceptionWhenTargetIdIsSameAndCouponIsNotExpired() {
        final Long targetId = 1L;
        Issuance issuance = IssuanceFixture.builder()
            .targetId(targetId)
            .issuedAt(ZonedDateTime.now())
            .expiredAt(ZonedDateTime.now().plusDays(1))
            .build();

        assertDoesNotThrow(() -> issuance.validate(targetId));
    }

}
