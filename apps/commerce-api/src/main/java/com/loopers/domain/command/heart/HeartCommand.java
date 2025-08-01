package com.loopers.domain.command.heart;

import static com.loopers.utils.ValidationUtils.requireNonNull;

import lombok.Builder;

public class HeartCommand {

    @Builder
    public record Create(
        Long userId,
        Target target
    ) {
        public Create {
            requireNonNull(userId, "사용자 ID가 null일 수 없습니다.");
            requireNonNull(target, "좋아요 대상이 null일 수 없습니다.");
        }
    }

}
