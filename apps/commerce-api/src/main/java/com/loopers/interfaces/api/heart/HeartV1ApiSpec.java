package com.loopers.interfaces.api.heart;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.heart.HeartV1Dto.GetList.Response;
import com.loopers.interfaces.api.shared.PaginationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;

@Tag(name = "Heart V1 API", description = "하트 관리 API 입니다.")
public interface HeartV1ApiSpec {

    @Operation(summary = "좋아요 추가", description = "좋아요를 추가합니다.")
    ApiResponse<?> addHeart(Long productId, @Parameter(hidden = true) String userId);

    @Operation(summary = "좋아요 제거", description = "좋아요를 제거합니다.")
    ApiResponse<?> removeHeart(Long productId, @Parameter(hidden = true) String userId);

    @Operation(summary = "내가 좋아요 한 상품 목록 조회", description = "내가 좋아요 한 상품 목록을 조회합니다.")
    ApiResponse<Page<Response>> getHeartList(
        @Parameter(description = "페이지 정보") PaginationRequest paginationRequest,
        @Parameter(hidden = true) String userId
    );
}
