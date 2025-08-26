package com.loopers.domain.user;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.shared.Money;
import com.loopers.fixture.UserRegisterCommandFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@UnitTest
class UserTest {

    @DisplayName("회원 도메인 생성 시, ")
    @Nested
    class Create {

        @DisplayName("유효한 회원 생성 명령을 전달하면 회원 도메인을 생성한다.")
        @Test
        void createMember() {
            var command = UserRegisterCommandFixture.builder().build();

            var member = User.of(command.accountId(), command.email(), command.birth(), command.gender());

            assertThat(member).isNotNull();
            assertThat(member.getAccountId().value()).isEqualTo(command.accountId());
            assertThat(member.getEmail().address()).isEqualTo(command.email());
            assertThat(member.getBirth().day()).isEqualTo(command.birth());
            assertThat(member.getTotalPoint()).isEqualTo(Money.ZERO);
            assertThat(member.getGender()).isEqualTo(command.gender());
        }
    }

    @DisplayName("회원 포인트 충전 시, ")
    @Nested
    class PointSourceTEmp {

        @DisplayName("포인트 충전 시 비용이 null인 경우 예외를 발생한다.")
        @ParameterizedTest
        @NullSource
        void throwsException_whenIncreaseBalanceCommandIsNull(Money invalidAmount) {
            var command = UserRegisterCommandFixture.builder().build();
            var member = User.of(command.accountId(), command.email(), command.birth(), command.gender());

            assertThatThrownBy(() -> member.chargePoint(invalidAmount))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("포인트 충전 시 비용이 0 이하인 경우 예외를 발생한다.")
        @ParameterizedTest
        @ValueSource(longs = {0L, -100L, -1000L})
        void throwsException_whenIncreaseBalanceAmountIsZeroOrNegative(Long invalidAmount) {
            var command = UserRegisterCommandFixture.builder().build();
            var member = User.of(command.accountId(), command.email(), command.birth(), command.gender());

            var chargeAmount = Money.of(invalidAmount);

            assertThatThrownBy(() -> member.chargePoint(chargeAmount))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("포인트 충전 시 비용이 가장 큰 경우 정상 충전된다.")
        @Test
        void increaseBalancePoint_whenMaxAmount() {
            var command = UserRegisterCommandFixture.builder().build();
            var member = User.of(command.accountId(), command.email(), command.birth(), command.gender());
            var chargeAmount = Money.of(Long.MAX_VALUE);

            member.chargePoint(chargeAmount);

            assertThat(member.getTotalPoint()).isEqualTo(chargeAmount);
        }

        @DisplayName("포인트 충전 시 비용이 가장 큰 경우 보다 큰 경우 예외가 발생한다.")
        @Test
        void increaseBalancePoint_whenOverflowAmount() {
            var command = UserRegisterCommandFixture.builder().build();
            var member = User.of(command.accountId(), command.email(), command.birth(), command.gender());
            var chargeAmount = Money.of(Long.MAX_VALUE);
            member.chargePoint(chargeAmount);

            assertThatThrownBy(() -> member.chargePoint(Money.of(1L)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("유효한 금액을 전달하면 포인트를 충전한다.")
        @Test
        void increaseBalancePoint_whenValidAmount() {
            var command = UserRegisterCommandFixture.builder().build();
            var member = User.of(command.accountId(), command.email(), command.birth(), command.gender());
            var chargeAmount = Money.of(1000L);

            member.chargePoint(chargeAmount);

            assertThat(member.getTotalPoint()).isEqualTo(chargeAmount);
        }
    }
}
