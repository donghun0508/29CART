package com.loopers.domain.query.product;

public class ProductQuery {

    public static class List {

        public record Condition(
            Long brandId,
            SortType sort
        ) {

            public static Condition ofDefault() {
                return new Condition(null, SortType.LATEST);
            }

            public enum SortType {
                LATEST,
                PRICE_ASC,
                LIKES_DESC
            }
        }

        public record Response(
            Long productId,
            ProductInfo productInfo
        ) {

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

    public static class Detail {

        public record Condition(
            Long productId,
            String userId
        ) {

        }

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
        }
    }
}
