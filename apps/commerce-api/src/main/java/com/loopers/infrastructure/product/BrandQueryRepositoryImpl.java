package com.loopers.infrastructure.product;

import com.loopers.domain.query.product.BrandQuery;
import com.loopers.domain.query.product.BrandQueryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class BrandQueryRepositoryImpl implements BrandQueryRepository {

    private final BrandJpaRepository brandJpaRepository;

    @Override
    public Optional<BrandQuery.Detail.Response> findBrandDetail(BrandQuery.Detail.Condition condition) {
        return brandJpaRepository.findBrandDetail(condition);
    }
}
