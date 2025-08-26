package com.loopers.interfaces.api.catalog;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.NoCountPageResponse;
import com.loopers.interfaces.api.PaginationRequest;
import com.loopers.interfaces.api.catalog.ProductV1Dto.GetListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product V1 API", description = "상품 관리 API 입니다.")
public interface ProductV1ApiSpec {

    @Operation(
        summary = "상품 조회",
        description = "상품 목록을 조회합니다."
    )
    ApiResponse<NoCountPageResponse<GetListResponse>> getProducts(ProductV1Dto.GetListRequest request,
        PaginationRequest paginationRequest);

    @Operation(
        summary = "상품 상세 조회",
        description = "상품의 상세 정보를 조회합니다."
    )
    ApiResponse<ProductV1Dto.GetDetailResponse> getProductDetail(Long productId, @Parameter(hidden = true) String userId);
}
