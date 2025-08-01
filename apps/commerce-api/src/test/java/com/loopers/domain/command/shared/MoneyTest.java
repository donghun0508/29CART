package com.loopers.domain.command.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.loopers.domain.command.shared.Money;
import com.loopers.domain.command.fixture.MoneyFixture;
import com.loopers.environment.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class MoneyTest {

    @DisplayName("Money 객체 생성 시,")
    @Nested
    class Create {

        @DisplayName("금액이 null이거나 음수이면 Money 객체 생성에 실패한다.")
        @ParameterizedTest
        @NullSource
        @ValueSource(longs = {-1L, -1000L})
        void throwsIllegalArgumentException_whenValueIsInvalid(Long invalidValue) {
            assertThatThrownBy(() -> new Money(invalidValue))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("금액이 0 이상이면 Money 객체 생성에 성공한다.")
        @ParameterizedTest
        @ValueSource(longs = {0L, 1L, 1000L, 999999L})
        void success_whenValueIsValid(Long validValue) {
            Money money = MoneyFixture.with(validValue);

            assertThat(money.value()).isEqualTo(validValue);
        }
    }

    @DisplayName("Money 덧셈 연산 시,")
    @Nested
    class Add {

        @DisplayName("덧셈 결과가 Long.MAX_VALUE를 초과하면 예외가 발생한다.")
        @Test
        void throwsIllegalArgumentException_whenOverflow() {
            Money money1 = MoneyFixture.with(Long.MAX_VALUE);
            Money money2 = MoneyFixture.with(1L);

            assertThatThrownBy(() -> money1.add(money2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(ArithmeticException.class);
        }

        @DisplayName("정상적인 두 금액을 더하면 올바른 결과를 반환한다.")
        @Test
        void success_whenAddingValidAmounts() {
            Money money1 = MoneyFixture.complete();
            Money money2 = MoneyFixture.complete();
            Long totalValue = money1.value() + money2.value();

            Money result = money1.add(money2);

            assertThat(result.value()).isEqualTo(totalValue);
        }

        @DisplayName("0원과 다른 금액을 더해도 올바른 결과를 반환한다.")
        @Test
        void success_whenAddingWithZero() {
            Money money = MoneyFixture.complete();
            Money zero = MoneyFixture.zero();

            Money result1 = money.add(zero);
            Money result2 = zero.add(money);

            assertThat(result1.value()).isEqualTo(money.value());
            assertThat(result2.value()).isEqualTo(money.value());
        }
    }

    @DisplayName("Money 뺄셈 연산 시,")
    @Nested
    class Subtract {

        @DisplayName("작은 금액에서 큰 금액을 빼면 예외가 발생한다.")
        @ParameterizedTest
        @CsvSource({
            "1000, 2000",
            "0, 1",
            "500, 1000"
        })
        void throwsIllegalArgumentException_whenResultWouldBeNegative(Long minuend, Long subtrahend) {
            Money money1 = MoneyFixture.with(minuend);
            Money money2 = MoneyFixture.with(subtrahend);

            assertThatThrownBy(() -> money1.subtract(money2))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("큰 금액에서 작은 금액을 빼면 올바른 결과를 반환한다.")
        @ParameterizedTest
        @CsvSource({
            "5000, 2000",
            "10, 1",
            "999999999, 999999998"
        })
        void success_whenSubtractingValidAmounts(Long minuend, Long subtrahend) {
            Money money1 = MoneyFixture.with(minuend);
            Money money2 = MoneyFixture.with(subtrahend);
            Long expectedValue = minuend - subtrahend;

            Money result = money1.subtract(money2);

            assertThat(result.value()).isEqualTo(expectedValue);
        }

        @DisplayName("같은 금액을 빼면 0원이 된다.")
        @Test
        void success_whenSubtractingSameAmount() {
            Money money = MoneyFixture.complete();

            Money result = money.subtract(money);

            assertThat(result).isEqualTo(Money.ZERO);
        }

        @DisplayName("0원에서 0원을 빼면 0원이 된다.")
        @Test
        void success_whenSubtractingZeroFromZero() {
            Money zero = MoneyFixture.zero();

            Money result = zero.subtract(zero);

            assertThat(result).isEqualTo(Money.ZERO);
        }
    }
}
