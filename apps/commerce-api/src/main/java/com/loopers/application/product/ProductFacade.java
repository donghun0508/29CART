package com.loopers.application.product;

import com.loopers.domain.query.product.ProductQuery;
import com.loopers.domain.query.product.ProductQuery.List.Response;
import com.loopers.domain.query.product.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductFacade {

    private final ProductQueryService productQueryService;

    public Page<ProductQuery.List.Response> getProducts(ProductQuery.List.Condition condition, Pageable pageable) {
        return productQueryService.findProducts(condition, pageable);
    }

    public ProductQuery.Detail.Response getProductDetail(ProductQuery.Detail.Condition condition) {
        return productQueryService.findProductDetail(condition);
    }
}
