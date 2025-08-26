package com.loopers.domain.catalog;

import jakarta.persistence.Embeddable;

@Embeddable
public record HeartCount(Long value) {

    public HeartCount up() {
        return new HeartCount(value + 1);
    }

    public HeartCount down() {
        if (this.value <= 0) {
            throw new IllegalStateException("좋아요 수가 0 이하입니다. 좋아요를 취소할 수 없습니다.");
        }
        return new HeartCount(this.value - 1);
    }

    public static HeartCount zero() {
        return new HeartCount(0L);
    }
}
