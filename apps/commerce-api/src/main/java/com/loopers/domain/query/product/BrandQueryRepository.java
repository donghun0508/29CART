package com.loopers.domain.query.product;

import java.util.Optional;

public interface BrandQueryRepository {

    Optional<BrandQuery.Detail.Response> findBrandDetail(BrandQuery.Detail.Condition condition);
}
