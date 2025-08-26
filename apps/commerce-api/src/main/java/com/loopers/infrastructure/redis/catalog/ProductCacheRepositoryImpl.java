package com.loopers.infrastructure.redis.catalog;

import com.loopers.domain.catalog.ProductCacheRepository;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.ProductSliceRead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductCacheRepositoryImpl implements ProductCacheRepository {

    private final ProductListRedisCacheRepository productListRedisCacheRepository;
    private final ProductDetailRedisCacheRepository productDetailRedisCacheRepository;

    @Override
    public ProductSliceRead findSliceByCondition(ListCondition condition) {
        return productListRedisCacheRepository.findByListCondition(condition);
    }

    @Override
    public void save(ListCondition condition, ProductSliceRead productSliceRead) {
        productListRedisCacheRepository.save(condition, productSliceRead);
    }

    @Override
    public ProductRead findById(Long productId) {
        return productDetailRedisCacheRepository.findById(productId);
    }

    @Override
    public void save(Long productId, ProductRead productRead) {
        productDetailRedisCacheRepository.save(productId, productRead);
    }
}
