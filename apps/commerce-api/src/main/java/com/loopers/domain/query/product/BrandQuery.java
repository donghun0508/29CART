package com.loopers.domain.query.product;

public class BrandQuery {

    public static class Detail {

        public record Condition(Long brandId) {

        }

        public record Response(
            Long brandId,
            String brandName,
            Long totalProductCount
        ) {

        }
    }

}
