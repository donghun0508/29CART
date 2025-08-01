package com.loopers.interfaces.api.heart;

import com.loopers.domain.query.heart.HeartQuery.List;

public class HeartV1Dto {

    public static class GetList {

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

            public static HeartV1Dto.GetList.Response from(List.Response response) {
                return new HeartV1Dto.GetList.Response(
                    new ProductInfo(
                        response.product().productName()
                        , response.product().isSoldOut()
                        , response.product().price()
                        , response.product().brandId()
                        , response.product().brandName()
                        , response.product().likeCount()
                    )
                );
            }
        }
    }
}
