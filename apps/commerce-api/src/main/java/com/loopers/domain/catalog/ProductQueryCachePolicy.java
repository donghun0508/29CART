package com.loopers.domain.catalog;

import com.loopers.domain.catalog.ProductCondition.ListCondition;
import org.springframework.stereotype.Component;

@Component
public class ProductQueryCachePolicy {

    public boolean shouldCache(ListCondition condition) {
        return condition.pageSize() <= 2;
    }

    public boolean shouldCache(Long productId) {
        return productId <= 10L;
    }
}
