package com.loopers.application.heart;

import com.loopers.application.heart.HeartCommand.LikeCommand;
import com.loopers.application.heart.HeartCommand.UnlikeCommand;
import com.loopers.application.heart.processor.TargetProcessor;
import com.loopers.domain.heart.Heart;
import com.loopers.domain.heart.HeartService;
import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.logging.support.analytics.EventTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Component
public class HeartFacade {

    private final HeartService heartService;
    private final UserService userService;
    private final TargetProcessor targetProcessor;
    private final DomainEventPublisher domainEventPublisher;

    @EventTrace
    @Transactional
    public void heart(LikeCommand criteria) {
        targetProcessor.acquireTargetLock(criteria.target());
        User user = userService.findByAccountId(criteria.accountId());
        Heart heart = Heart.from(user.getId(), criteria.target());
        heartService.create(heart);
        domainEventPublisher.publishEvent(heart.events());
    }

    @EventTrace
    @Transactional
    public void unHeart(UnlikeCommand criteria) {
        targetProcessor.acquireTargetLock(criteria.target());
        User user = userService.findByAccountId(criteria.accountId());
        Heart heart = heartService.findByUserIdAndTarget(user.getId(), criteria.target());
        heart.remove();
        heartService.delete(heart);
        domainEventPublisher.publishEvent(heart.events());
    }

    public Page<HeartResult> getHeartList(String userId, Pageable pageable) {
        User user = userService.findByAccountId(AccountId.of(userId));
        Page<Heart> hearts = heartService.getHeartList(user.getId(), pageable);
        return hearts.map(HeartResult::from);
    }
}
