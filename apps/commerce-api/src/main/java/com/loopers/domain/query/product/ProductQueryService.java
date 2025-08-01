package com.loopers.domain.query.product;

import static com.loopers.support.error.ErrorType.NOT_FOUND;

import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public Page<ProductQuery.List.Response> findProducts(ProductQuery.List.Condition condition, Pageable pageable) {
        return productQueryRepository.findProducts(condition, pageable);
    }

    public ProductQuery.Detail.Response findProductDetail(ProductQuery.Detail.Condition condition) {
        return productQueryRepository.findProductDetail(condition).orElseThrow(() -> new CoreException(NOT_FOUND));
    }
}
