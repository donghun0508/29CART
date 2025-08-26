package com.loopers.interfaces.api.catalog;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

import com.loopers.application.catalog.ProductFacade;
import com.loopers.application.catalog.ProductResult.ProductDetailResult;
import com.loopers.application.catalog.ProductResult.ProductSliceResult;
import com.loopers.domain.catalog.ProductCondition.DetailCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.user.AccountId;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.NoCountPageResponse;
import com.loopers.interfaces.api.PaginationRequest;
import com.loopers.interfaces.api.catalog.ProductV1Dto.GetListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller implements ProductV1ApiSpec {

    private final ProductFacade productFacade;

    @Override
    @GetMapping
    public ApiResponse<NoCountPageResponse<GetListResponse>> getProducts(
        @ModelAttribute ProductV1Dto.GetListRequest request,
        @ModelAttribute PaginationRequest paginationRequest
    ) {
        Slice<ProductSliceResult> products = productFacade.getProductList(
            new ListCondition(request.brandId(), request.sort(), paginationRequest.toPageable()));
        return ApiResponse.success(NoCountPageResponse.of(products.map(ProductV1Dto.GetListResponse::from)));
    }

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<ProductV1Dto.GetDetailResponse> getProductDetail(
        @PathVariable Long productId,
        @RequestHeader(value = USER_ID, required = false) String userId
    ) {
        ProductDetailResult productDetail = productFacade.getProductDetail(new DetailCondition(productId, AccountId.of(userId)));
        return ApiResponse.success(ProductV1Dto.GetDetailResponse.from(productDetail));
    }
}
