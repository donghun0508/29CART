package com.loopers.domain.catalog;

import com.loopers.domain.catalog.ProductCondition.ListCondition;
import java.util.Set;

public interface ProductCacheRepository {

    ProductSliceRead findSliceByCondition(ListCondition condition);

    void save(ListCondition condition, ProductSliceRead productSliceRead);

    ProductRead findById(Long productId);

    void save(Long productId, ProductRead productRead);

    Set<String> find(String key, int start, int end);

    Integer findRank(String key, Long productId);
}
