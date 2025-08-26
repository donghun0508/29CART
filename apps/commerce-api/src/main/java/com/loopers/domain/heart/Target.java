package com.loopers.domain.heart;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record Target(Long targetId, @Enumerated(value = EnumType.STRING) TargetType targetType) {

    public Target {
        if (targetId == null) {
            throw new IllegalArgumentException("대상 ID가 null일 수 없습니다.");
        }
        if (targetType == null) {
            throw new IllegalArgumentException("대상 타입이 null일 수 없습니다.");
        }
    }

    public static Target of(Long targetId, TargetType targetType) {
        return new Target(targetId, targetType);
    }

    public static Target asProduct(Long productId) {
        return new Target(productId, TargetType.PRODUCT);
    }

}
