package com.loopers.infrastructure.product;

import com.loopers.domain.query.product.BrandQuery;
import java.util.Optional;

public interface CustomBrandQueryRepository {

    Optional<BrandQuery.Detail.Response> findBrandDetail(BrandQuery.Detail.Condition condition);
}
