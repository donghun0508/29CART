package com.loopers.interfaces.api.heart;


import com.loopers.application.heart.HeartResult;

public class HeartV1Dto {

    public record Response(ProductInfo product) {

        public static Response from(HeartResult heartResult) {
            return new Response(ProductInfo.from(heartResult.product()));
        }

        public record ProductInfo(
            String productName,
            boolean isSoldOut,
            Long price,
            Long brandId,
            String brandName,
            Long likeCount
        ) {

            public static ProductInfo from(HeartResult.ProductInfo product) {
                return new ProductInfo(
                    product.productName(),
                    product.isSoldOut(),
                    product.price(),
                    product.brandId(),
                    product.brandName(),
                    product.likeCount()
                );
            }
        }
    }
}
