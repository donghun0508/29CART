package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductFacade;
import com.loopers.domain.query.product.ProductQuery;
import com.loopers.interfaces.api.ApiHeaders;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductV1Dto.GetDetail;
import com.loopers.interfaces.api.product.ProductV1Dto.GetList;
import com.loopers.interfaces.api.shared.PaginationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ApiResponse<Page<GetList.Response>> getProducts(
        @ModelAttribute GetList.Request request,
        @ModelAttribute PaginationRequest paginationRequest
    ) {
        Page<ProductQuery.List.Response> responses = productFacade.getProducts(request.getCondition(),
            paginationRequest.toPageable());
        return ApiResponse.success(responses.map(ProductV1Dto.GetList.Response::from));
    }

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<GetDetail.Response> getProductDetail(
        @PathVariable Long productId,
        @RequestHeader(value = ApiHeaders.USER_ID, required = false) String userId
    ) {
        ProductQuery.Detail.Condition condition = new ProductQuery.Detail.Condition(productId, userId);
        ProductQuery.Detail.Response response = productFacade.getProductDetail(condition);
        return ApiResponse.success(GetDetail.Response.from(response));
    }
}
