package com.loopers.infrastructure.heart;

import com.loopers.domain.query.heart.HeartQuery.List.Condition;
import com.loopers.domain.query.heart.HeartQuery.List.Response;
import com.loopers.domain.query.heart.HeartQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class HeartQueryRepositoryImpl implements HeartQueryRepository {

    private final HeartJpaRepository heartJpaRepository;

    @Override
    public Page<Response> getHeartList(Condition condition, Pageable pageable) {
        return heartJpaRepository.getHeartList(condition, pageable);
    }
}
