package com.loopers.application.heart;

import com.loopers.domain.heart.Target;
import com.loopers.domain.heart.TargetType;
import com.loopers.domain.user.AccountId;

public class HeartCommand {

    public record LikeCommand(AccountId accountId, Target target) {

        public LikeCommand(String accountId, Long targetId, TargetType targetType) {
            this(AccountId.of(accountId), Target.of(targetId, targetType));
        }

        public static LikeCommand of(String accountId, Long targetId, TargetType targetType) {
            return new LikeCommand(accountId, targetId, targetType);
        }
    }

    public record UnlikeCommand(AccountId accountId, Target target) {

        public UnlikeCommand(String accountId, Long targetId, TargetType targetType) {
            this(AccountId.of(accountId), Target.of(targetId, targetType));
        }

        public static UnlikeCommand of(String accountId, Long productId, TargetType targetType) {
            return new UnlikeCommand(accountId, productId, targetType);
        }
    }

}
