package com.loopers.infrastructure.product;

import com.loopers.domain.query.product.ProductQuery;
import com.loopers.domain.query.product.ProductQuery.List.Condition;
import com.loopers.domain.query.product.ProductQueryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ProductQueryRepositoryImpl implements ProductQueryRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Page<ProductQuery.List.Response> findProducts(Pageable pageable) {
        return findProducts(Condition.ofDefault(), pageable);
    }

    @Override
    public Page<ProductQuery.List.Response> findProducts(ProductQuery.List.Condition condition, Pageable pageable) {
        return productJpaRepository.findProducts(condition, pageable);
    }

    @Override
    public Optional<ProductQuery.Detail.Response> findProductDetail(ProductQuery.Detail.Condition condition) {
        return productJpaRepository.findProductDetail(condition);
    }
}
