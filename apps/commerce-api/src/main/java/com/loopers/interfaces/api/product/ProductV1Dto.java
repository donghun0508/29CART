package com.loopers.interfaces.api.product;

import com.loopers.domain.query.product.ProductQuery;
import com.loopers.domain.query.product.ProductQuery.List;
import com.loopers.domain.query.product.ProductQuery.List.Condition.SortType;
import lombok.Data;

public class ProductV1Dto {

    public static class GetList {

        @Data
        public static class Request {

            private Long brandId;
            private SortType sort;

            public ProductQuery.List.Condition getCondition() {
                if (brandId == null && sort == null) {
                    return ProductQuery.List.Condition.ofDefault();
                }
                SortType finalSort = sort != null ? sort : SortType.LATEST;
                return new ProductQuery.List.Condition(brandId, finalSort);
            }
        }

        public record Response(
            Long productId,
            List.Response.ProductInfo productInfo
        ) {

            public static ProductV1Dto.GetList.Response from(ProductQuery.List.Response response) {
                return new ProductV1Dto.GetList.Response(
                    response.productId(),
                    new List.Response.ProductInfo(
                        response.productInfo().productName(),
                        response.productInfo().isSoldOut(),
                        response.productInfo().price(),
                        response.productInfo().brandId(),
                        response.productInfo().brandName(),
                        response.productInfo().likeCount()
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

    public static class GetDetail {

        public record Response(
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

            public static GetDetail.Response from(ProductQuery.Detail.Response response) {
                return new GetDetail.Response(
                    response.productId(),
                    new ProductDetailInfo(
                        response.product().productName(),
                        response.product().isSoldOut(),
                        response.product().price(),
                        response.product().likeCount(),
                        response.product().isLiked()
                    ),
                    new BrandDetailInfo(
                        response.brand().brandId(),
                        response.brand().brandName()
                    )
                );
            }
        }
    }
}
