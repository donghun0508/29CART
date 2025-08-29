package com.loopers.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

import com.loopers.config.annotations.UnitTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

@UnitTest
class CouponStateTest {

    @DisplayName("쿠폰이 사용 가능한 상태가 아닌 경우, 쿠폰을 사용했을 때 예외가 발생한다.")
    @Test
    void throwsExceptionWhenUseCouponInInvalidState() {
        CouponState issuedState = new CouponState(CouponStatus.INVALID, LocalDateTime.now());

        assertThatThrownBy(issuedState::used)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("CouponState.used().status");
    }

    @DisplayName("쿠폰이 사용된 상태가 아닌 경우, 쿠폰을 취소했을 때 예외가 발생한다.")
    @Test
    void throwsExceptionWhenCancelCouponInInvalidState() {
        CouponState issuedState = new CouponState(CouponStatus.AVAILABLE, LocalDateTime.now());

        assertThatThrownBy(issuedState::canceled)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("CouponState.canceled()");
    }


    @DisplayName("쿠폰이 사용 가능한 상태가 아닌 경우, 쿠폰 사용 가능 여부를 확인했을 때 예외가 발생한다.")
    @Test
    void throwsExceptionWhenCheckAvailableInInvalidState() {
        CouponState issuedState = new CouponState(CouponStatus.USED, LocalDateTime.now());

        assertThatThrownBy(issuedState::checkAvailable)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("CouponState.isAvailable()");
    }

    @DisplayName("쿠폰이 사용 가능한 상태인 경우, 쿠폰을 사용했을 때 상태가 USED로 변경된다.")
    @Test
    void changeStateToUsedWhenUseCouponInValidState() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        CouponState issuedState = new CouponState(CouponStatus.AVAILABLE, null);

        try (MockedStatic<LocalDateTime> mockedTime = mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(fixedTime);

            CouponState usedState = issuedState.used();

            assertThat(usedState.status()).isEqualTo(CouponStatus.USED);
            assertThat(usedState.usedAt()).isEqualTo(fixedTime);
        }
    }

    @DisplayName("쿠폰이 사용된 상태인 경우, 쿠폰을 취소했을 때 상태가 AVAILABLE로 변경된다.")
    @Test
    void changeStateToAvailableWhenCancelCouponInValidState() {
        CouponState usedState = new CouponState(CouponStatus.USED, LocalDateTime.now());

        CouponState canceledState = usedState.canceled();

        assertThat(canceledState.status()).isEqualTo(CouponStatus.AVAILABLE);
        assertThat(canceledState.usedAt()).isNull();
    }


}
