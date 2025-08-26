package com.loopers.domain.catalog;

import com.loopers.domain.catalog.ProductCondition.ListCondition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Slice;

public interface ProductRepository {

    Product save(Product product);

    boolean existsById(Long targetId);

    Optional<Product> findById(Long productId);

    Optional<Product> findByIdWithOptimisticLock(Long productId);

    Optional<Product> findByIdWithLock(Long productId);

    List<Product> findAllByIdIn(List<Long> productIds);

    List<Product> findAllByIdInWithLock(List<Long> productIds);

    Slice<Product> findSliceByCondition(ListCondition condition);

    Long countByBrandId(Long brandId);
}
