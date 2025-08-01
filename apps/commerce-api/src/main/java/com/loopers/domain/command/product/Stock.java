package com.loopers.domain.command.product;

import jakarta.persistence.Embeddable;

@Embeddable
public record Stock(Long count) {

    public Stock {
        if (count == null || count < 0) {
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다");
        }
    }

    public Stock add(Stock other) {
        try {
            return new Stock(Math.addExact(this.count, other.count));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("재고 수량 계산 중 오버플로우가 발생했습니다", e);
        }
    }

    public Stock subtract(Stock other) {
        if (this.count < other.count) {
            throw new IllegalArgumentException("계산 결과가 음수가 될 수 없습니다");
        }
        return new Stock(this.count - other.count);
    }

    public boolean isGreaterThan(Stock other) {
        return this.count > other.count;
    }

    public boolean isLessThan(Stock other) {
        return this.count < other.count;
    }

    public boolean isGreaterThanOrEqual(Stock other) {
        return this.count >= other.count;
    }

    public boolean isLessThanOrEqual(Stock other) {
        return this.count <= other.count;
    }

    public boolean isZero() {
        return this.count.equals(0L);
    }

    public boolean isPositive() {
        return this.count > 0;
    }

    public static Stock of(long count) {
        return new Stock(count);
    }

    @Override
    public String toString() {
        return String.format("%,d개", count);
    }
}
