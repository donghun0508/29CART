package com.loopers.interfaces.api.product;

import com.loopers.domain.query.product.BrandQuery;

public class BrandV1Dto {

    public static class GetDetail {

        public record Response(
            Long brandId,
            String brandName,
            Long totalProductCount
        ) {

            public static BrandV1Dto.GetDetail.Response from(BrandQuery.Detail.Response response) {
                return new BrandV1Dto.GetDetail.Response(
                    response.brandId(),
                    response.brandName(),
                    response.totalProductCount()
                );
            }
        }
    }
}
