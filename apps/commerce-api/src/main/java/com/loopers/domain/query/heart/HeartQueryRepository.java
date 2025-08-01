package com.loopers.domain.query.heart;

import com.loopers.domain.query.heart.HeartQuery.List.Condition;
import com.loopers.domain.query.heart.HeartQuery.List.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HeartQueryRepository {

    Page<Response> getHeartList(Condition condition, Pageable pageable);
}
