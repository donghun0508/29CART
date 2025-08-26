package com.loopers.infrastructure.persistence.catalog;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public boolean existsById(Long targetId) {
        return productJpaRepository.existsById(targetId);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public Optional<Product> findByIdWithOptimisticLock(Long productId) {
        return productJpaRepository.findByIdWithOptimisticLock(productId);
    }

    @Override
    public Optional<Product> findByIdWithLock(Long productId) {
        return productJpaRepository.findByIdWithLock(productId);
    }

    @Override
    public List<Product> findAllByIdIn(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds);
    }

    @Override
    public List<Product> findAllByIdInWithLock(List<Long> productIds) {
        return productJpaRepository.findAllByIdInWithLock(productIds);
    }

    @Override
    public Slice<Product> findSliceByCondition(ListCondition condition) {
        return productJpaRepository.findSliceByBrandId(condition);
    }

    @Override
    public Long countByBrandId(Long brandId) {
        return productJpaRepository.countByBrandId(brandId);
    }
}
