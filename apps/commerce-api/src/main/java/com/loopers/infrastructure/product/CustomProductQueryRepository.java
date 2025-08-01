package com.loopers.infrastructure.product;

import com.loopers.domain.query.product.ProductQuery;
import com.loopers.domain.query.product.ProductQuery.Detail.Response;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductQueryRepository {

    Page<ProductQuery.List.Response> findProducts(Pageable pageable);

    Page<ProductQuery.List.Response> findProducts(ProductQuery.List.Condition condition, Pageable pageable);

    Optional<ProductQuery.Detail.Response> findProductDetail(ProductQuery.Detail.Condition condition);
}
