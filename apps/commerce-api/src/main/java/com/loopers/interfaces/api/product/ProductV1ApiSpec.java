package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductV1Dto.GetList.Response;
import com.loopers.interfaces.api.shared.PaginationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;

@Tag(name = "Product V1 API", description = "상품 관리 API 입니다.")
public interface ProductV1ApiSpec {

    @Operation(
        summary = "상품 조회",
        description = "상품 목록을 조회합니다."
    )
    ApiResponse<Page<Response>> getProducts(ProductV1Dto.GetList.Request request, PaginationRequest paginationRequest);

    @Operation(
        summary = "상품 상세 조회",
        description = "상품의 상세 정보를 조회합니다."
    )
    ApiResponse<ProductV1Dto.GetDetail.Response> getProductDetail(Long productId, @Parameter(hidden = true) String userId);
}
