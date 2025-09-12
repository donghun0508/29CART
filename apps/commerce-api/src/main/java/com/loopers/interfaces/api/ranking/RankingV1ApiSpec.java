package com.loopers.interfaces.api.ranking;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.NoCountPageResponse;
import com.loopers.interfaces.api.PaginationRequest;
import com.loopers.interfaces.api.ranking.RankingV1Dto.GetRankingListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ranking V1 API", description = "랭킹 API 입니다.")
public interface RankingV1ApiSpec {

    @Operation(
        summary = "랭킹 조회",
        description = "상품 인기 목록을 조회합니다."
    )
    ApiResponse<NoCountPageResponse<GetRankingListResponse>> getHotProducts(RankingV1Dto.GetListRequest request,
        PaginationRequest paginationRequest);
}
