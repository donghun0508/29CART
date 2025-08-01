package com.loopers.domain.command.shared;


import jakarta.persistence.Embeddable;

@Embeddable
public record Money(Long value) {

    public static final Money ZERO = new Money(0L);

    public Money {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다");
        }
    }

    public Money add(Money other) {
        try {
            return new Money(Math.addExact(this.value, other.value));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("금액 계산 중 오버플로우가 발생했습니다", e);
        }
    }

    public Money subtract(Money other) {
        if (this.value < other.value) {
            throw new IllegalArgumentException("계산 결과가 음수가 될 수 없습니다");
        }
        return new Money(this.value - other.value);
    }

    public Money multiply(long multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("곱셈은 0 이상의 수만 가능합니다");
        }
        try {
            return new Money(Math.multiplyExact(this.value, multiplier));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("금액 계산 중 오버플로우가 발생했습니다", e);
        }
    }

    public Money divide(long divisor) {
        if (divisor <= 0) {
            throw new IllegalArgumentException("나눗셈은 양수만 가능합니다");
        }
        return new Money(this.value / divisor);
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

    public static Money of(long value) {
        return new Money(value);
    }

    @Override
    public String toString() {
        return String.format("%,d원", value);
    }
}
