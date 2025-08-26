package com.loopers.infrastructure.persistence.catalog;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import org.springframework.data.domain.Slice;

public interface CustomProductJpaRepository {

    Slice<Product> findSliceByBrandId(ListCondition condition);
}
