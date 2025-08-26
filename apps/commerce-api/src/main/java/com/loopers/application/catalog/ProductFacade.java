package com.loopers.application.catalog;

import com.loopers.application.catalog.ProductResult.ProductDetailResult;
import com.loopers.application.catalog.ProductResult.ProductSliceResult;
import com.loopers.domain.catalog.ProductCondition.DetailCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.catalog.ProductSliceRead;
import com.loopers.domain.heart.HeartService;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductFacade {

    private final ProductService productService;
    private final UserService userService;
    private final HeartService heartService;

    public Slice<ProductSliceResult> getProductList(ListCondition condition) {
        ProductSliceRead productSliceRead = productService.getProductSliceRead(condition);
        return ProductSliceResult.from(productSliceRead);
    }

    public ProductDetailResult getProductDetail(DetailCondition condition) {
        ProductRead productRead = productService.getProductRead(condition.productId());

        productRead.setLiked(userService.lookupByAccountId(condition.accountId())
            .map(user -> heartService.isLikedBy(user.getId(), productRead.toTarget()))
            .orElse(false));

        return ProductDetailResult.from(productRead);
    }
}
