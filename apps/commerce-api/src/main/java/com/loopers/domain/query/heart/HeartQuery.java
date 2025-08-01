package com.loopers.domain.query.heart;

public class HeartQuery {

    public static class List {
        public record Condition(String userId) {

        }

        public record Response(
            ProductInfo product
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

}
