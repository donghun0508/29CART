package com.loopers.domain.query.heart;

import com.loopers.domain.query.heart.HeartQuery.List.Condition;
import com.loopers.domain.query.heart.HeartQuery.List.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HeartQueryService {

    private final HeartQueryRepository heartQueryRepository;

    public Page<Response> getHeartList(Condition condition, Pageable pageable) {
        return heartQueryRepository.getHeartList(condition, pageable);
    }
}
