package com.loopers.application.heart;

import com.loopers.domain.command.heart.HeartCommand;
import com.loopers.domain.command.heart.HeartService;
import com.loopers.domain.command.heart.Target;
import com.loopers.domain.command.user.User;
import com.loopers.domain.command.user.UserService;
import com.loopers.domain.query.heart.HeartQuery.List.Condition;
import com.loopers.domain.query.heart.HeartQuery.List.Response;
import com.loopers.domain.query.heart.HeartQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class HeartFacade {

    private final UserService userService;
    private final HeartService heartService;
    private final HeartQueryService heartQueryService;
    private final TargetValidateService targetValidateService;

    public void like(String userId, Target target) {
        try {
            User user = userService.findByUserId(userId);
            targetValidateService.validate(target);

            HeartCommand.Create command = new HeartCommand.Create(user.getId(), target);
            heartService.create(command);
        } catch (DataIntegrityViolationException e) {
            log.info("이미 좋아요를 눌렀습니다. userId: {}, target: {}", userId, target);
        }
    }

    public void unlike(String userId, Target target) {
        User user = userService.findByUserId(userId);
        heartService.deleteByUserIdAndTarget(user.getId(), target);
    }

    void likeUnsafe(String userId, Target target) {
        User user = userService.findByUserId(userId);

        duplicateHeart(target, user);
        targetValidateService.validate(target);

        HeartCommand.Create command = new HeartCommand.Create(user.getId(), target);
        heartService.create(command);
    }

    private void duplicateHeart(Target target, User user) {
        if (heartService.existsByUserIdAndTarget(user.getId(), target)) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }
    }

    public Page<Response> getHeartList(Condition condition, Pageable pageable) {
        return heartQueryService.getHeartList(condition, pageable);
    }
}
