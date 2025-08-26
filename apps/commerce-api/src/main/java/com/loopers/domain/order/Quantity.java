package com.loopers.domain.order;

import jakarta.persistence.Embeddable;

@Embeddable
public record Quantity(Long count) {

    public Quantity {
        if (count == null || count < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다");
        }
    }

    public Quantity add(Quantity other) {
        try {
            return new Quantity(Math.addExact(this.count, other.count));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("수량 계산 중 오버플로우가 발생했습니다", e);
        }
    }

    public Quantity subtract(Quantity other) {
        if (this.count < other.count) {
            throw new IllegalArgumentException("계산 결과가 음수가 될 수 없습니다");
        }
        return new Quantity(this.count - other.count);
    }

    public boolean isGreaterThan(Quantity other) {
        return this.count > other.count;
    }

    public boolean isLessThan(Quantity other) {
        return this.count < other.count;
    }

    public boolean isGreaterThanOrEqual(Quantity other) {
        return this.count >= other.count;
    }

    public boolean isLessThanOrEqual(Quantity other) {
        return this.count <= other.count;
    }

    public boolean isZero() {
        return this.count.equals(0L);
    }

    public boolean isPositive() {
        return this.count > 0;
    }

    public static Quantity of(long count) {
        return new Quantity(count);
    }
}
