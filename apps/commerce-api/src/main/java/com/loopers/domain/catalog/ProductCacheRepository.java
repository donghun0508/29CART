package com.loopers.domain.catalog;

import com.loopers.domain.catalog.ProductCondition.ListCondition;

public interface ProductCacheRepository {

    ProductSliceRead findSliceByCondition(ListCondition condition);

    void save(ListCondition condition, ProductSliceRead productSliceRead);

    ProductRead findById(Long productId);

    void save(Long productId, ProductRead productRead);
}
