package com.loopers.interfaces.api.catalog;


import com.loopers.application.catalog.ProductResult.ProductDetailResult;
import com.loopers.application.catalog.ProductResult.ProductSliceResult;
import com.loopers.domain.catalog.SortType;

public class ProductV1Dto {

    public record GetListRequest(Long brandId, SortType sort) {

    }


    public record GetListResponse(
        Long productId,
        ProductInfo productInfo
    ) {

        public static GetListResponse from(ProductSliceResult result) {
            return new GetListResponse(
                result.productId(),
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

    public record GetDetailResponse(
        Long productId,
        ProductDetailInfo product,
        BrandDetailInfo brand
    ) {

        public record ProductDetailInfo(
            String productName,
            boolean isSoldOut,
            Long price,
            Long likeCount,
            boolean isLiked
        ) {

        }

        public record BrandDetailInfo(
            Long brandId,
            String brandName
        ) {

        }

        public static GetDetailResponse from(ProductDetailResult result) {
            return new GetDetailResponse(
                result.productId(),
                new ProductDetailInfo(
                    result.product().productName(),
                    result.product().isSoldOut(),
                    result.product().price(),
                    result.product().likeCount(),
                    result.product().isLiked()
                ),
                new BrandDetailInfo(
                    result.brand().brandId(),
                    result.brand().brandName()
                )
            );
        }
    }


}
