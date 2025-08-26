package com.loopers.domain.catalog;

import static com.loopers.domain.shared.Preconditions.isHit;

import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.logging.execution.ExecutionTime;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCacheRepository productCacheRepository;
    private final ProductQueryCachePolicy productQueryCachePolicy;

    @Transactional(readOnly = true)
    public List<Product> findAll(List<Long> productIds) {
        List<Product> products = productRepository.findAllByIdIn(productIds);
        List<Long> foundIds = products.stream()
            .map(Product::getId)
            .toList();
        List<Long> missingIds = productIds.stream()
            .filter(id -> !foundIds.contains(id))
            .toList();
        if (!missingIds.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "ProductService.findAll(): 존재하지 않는 상품: " + missingIds);
        }
        return products;
    }

    @Transactional
    public List<Product> findAllWithLock(List<Long> productIds) {
        List<Product> products = productRepository.findAllByIdInWithLock(productIds);

        List<Long> foundIds = products.stream()
            .map(Product::getId)
            .toList();
        List<Long> missingIds = productIds.stream()
            .filter(id -> !foundIds.contains(id))
            .toList();

        if (!missingIds.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND,
                "ProductService.findAllWithLock(): 존재하지 않는 상품: " + missingIds);
        }
        return products;
    }

    @Transactional(readOnly = true)
    public Product findById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "ProductService.findById(): 상품을 찾을 수 없습니다. 상품 ID: " + productId));
    }

    @Transactional
    public Product findByIdWithLock(Long productId) {
        return productRepository.findByIdWithLock(productId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                "ProductService.findByIdWithOptimisticLock(): 상품을 찾을 수 없습니다. 상품 ID: " + productId));
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    public ProductSliceRead getProductSliceRead(ListCondition condition) {
        ProductSliceRead cache = productCacheRepository.findSliceByCondition(condition);
        if (isHit(cache)) {
            return cache;
        }

        Slice<Product> products = productRepository.findSliceByCondition(condition);
        ProductSliceRead productSliceRead = ProductSliceRead.from(products);

        if (productQueryCachePolicy.shouldCache(condition)) {
            productCacheRepository.save(condition, productSliceRead);
        }

        return productSliceRead;
    }

    @Transactional(readOnly = true)
    public ProductRead getProductRead(Long productId) {
        ProductRead cache = productCacheRepository.findById(productId);
        if (isHit(cache)) {
            return cache;
        }

        ProductRead productRead = productRepository.findById(productId)
            .map(ProductRead::from)
            .orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "ProductService.findById(): 상품을 찾을 수 없습니다. 상품 ID: " + productId));

        if (productQueryCachePolicy.shouldCache(productId)) {
            productCacheRepository.save(productId, productRead);
        }

        return productRead;
    }

    public Long getCountByBrandId(Long brandId) {
        return productRepository.countByBrandId(brandId);
    }
}
