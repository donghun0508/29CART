package com.loopers.interfaces.api.ranking;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

import com.loopers.application.catalog.ProductFacade;
import com.loopers.application.catalog.ProductResult.ProductDetailResult;
import com.loopers.application.catalog.ProductResult.ProductSliceRankingResult;
import com.loopers.application.catalog.ProductResult.ProductSliceResult;
import com.loopers.domain.catalog.ProductCondition.DetailCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.NoCountPageResponse;
import com.loopers.interfaces.api.PaginationRequest;
import com.loopers.interfaces.api.catalog.ProductV1Dto;
import com.loopers.interfaces.api.catalog.ProductV1Dto.GetListResponse;
import com.loopers.interfaces.api.ranking.RankingV1Dto.GetRankingListResponse;
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
@RequestMapping("/api/v1/rankings")
public class RankingV1Controller implements RankingV1ApiSpec {

    private final ProductFacade productFacade;

    @Override
    @GetMapping
    public ApiResponse<NoCountPageResponse<GetRankingListResponse>> getHotProducts(
        @ModelAttribute RankingV1Dto.GetListRequest request,
        @ModelAttribute PaginationRequest paginationRequest
    ) {
        Slice<ProductSliceRankingResult> products = productFacade.getHotProducts(request.date(), paginationRequest.toPageable());
        return ApiResponse.success(NoCountPageResponse.of(products.map(GetRankingListResponse::from)));
    }
}
