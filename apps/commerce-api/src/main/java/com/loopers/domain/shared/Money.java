package com.loopers.domain.shared;

import static com.loopers.domain.shared.Preconditions.requireNonNull;
import static com.loopers.domain.shared.Preconditions.requirePositive;

import jakarta.persistence.Embeddable;

@Embeddable
public record Money(Long value) {

    public static final Money ZERO = new Money(0L);

    public Money {
        requireNonNull(value, "금액은 null일 수 없습니다");
    }

    public static Money of(final Long value) {
        return new Money(value);
    }

    public Money add(Money other) {
        try {
            requireNonNull(other, "더할 금액은 null일 수 없습니다");
            return new Money(Math.addExact(this.value, other.value));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("금액 계산 중 오버플로우가 발생했습니다", e);
        }
    }

    public Money subtract(Money other) {
        requireNonNull(other, "뺄 금액은 null일 수 없습니다");
        if (this.value < other.value) {
            throw new IllegalArgumentException("Money.subtract().other: 계산 결과가 음수가 될 수 없습니다");
        }
        return new Money(this.value - other.value);
    }

    public Money multiply(Long multiplier) {
        requirePositive(multiplier, "곱셈은 0 이상의 수만 가능합니다");
        try {
            return new Money(Math.multiplyExact(this.value, multiplier));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("금액 계산 중 오버플로우가 발생했습니다", e);
        }
    }

    public Money multiply(Double multiplier) {
        requireNonNull(multiplier, "곱셈에 사용되는 수는 null일 수 없습니다");
        if (multiplier < 0) {
            throw new IllegalArgumentException("곱셈은 0 이상의 수만 가능합니다");
        }
        return new Money(Math.round(this.value * multiplier));
    }

    public Money divide(Long divisor) {
        requirePositive(divisor, "나누는 수는 0 이상의 수여야 합니다");
        return new Money(this.value / divisor);
    }

    public Money min(Money other) {
        requireNonNull(other, "비교할 금액은 null일 수 없습니다");
        return this.value <= other.value ? this : other;
    }

    public boolean isNegativeOrZero() {
        return this.value <= 0;
    }

    public boolean isGreaterThan(Money other) {
        return this.value > other.value;
    }

    public boolean isLessThan(Money other) {
        return this.value < other.value;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return this.value >= other.value;
    }

    public boolean isLessThanOrEqual(Money other) {
        return this.value <= other.value;
    }

    public boolean isZero() {
        return this.value.equals(0L);
    }

    public boolean isPositive() {
        return this.value > 0;
    }
}
