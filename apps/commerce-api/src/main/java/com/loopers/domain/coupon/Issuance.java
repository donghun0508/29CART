package com.loopers.domain.coupon;

import static java.util.Objects.nonNull;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.ZonedDateTime;

@Embeddable
public record Issuance(@Enumerated(EnumType.STRING) TargetScope targetScope,
                       Long targetId,
                       ZonedDateTime issuedAt,
                       ZonedDateTime expiredAt
) {

    public void validate(Long targetId) {
        if (!this.targetId.equals(targetId)) {
            throw new IllegalStateException("Issuance.validate().targetId: 쿠폰 소유자가 아닙니다.");
        }
        if (nonNull(expiredAt) && expiredAt.isBefore(ZonedDateTime.now())) {
            throw new IllegalStateException("Issuance.validate().expiredAt: 쿠폰이 만료되었습니다.");
        }
    }
}
