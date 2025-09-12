package com.loopers.interfaces.api.ranking;

import com.loopers.application.catalog.ProductResult.ProductSliceRankingResult;

public class RankingV1Dto {

    public record GetListRequest(
        String date
    ) {

    }

    public record GetRankingListResponse(
        Long productId,
        int rank,
        ProductInfo productInfo
    ) {
        public static GetRankingListResponse from(ProductSliceRankingResult result) {
            return new GetRankingListResponse(
                result.productId(),
                result.rank(),
                new ProductInfo(
                    result.productInfo().productName(),
                    result.productInfo().isSoldOut(),
                    result.productInfo().price(),
                    result.productInfo().brandId(),
                    result.productInfo().brandName(),
                    result.productInfo().likeCount()
                )
            );
        }

        public record ProductInfo(
            String productName,
            boolean isSoldOut,
            Long price,
            Long brandId,
            String brandName,
            Long likeCount
        ) {

        }
    }
}
