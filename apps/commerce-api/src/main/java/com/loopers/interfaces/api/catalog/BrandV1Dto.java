package com.loopers.interfaces.api.catalog;


import com.loopers.application.catalog.BrandResult.BrandDetailResult;

public class BrandV1Dto {

    public record GetDetail(Long brandId, String brandName, Long totalProductCount) {

        public static GetDetail from(BrandDetailResult result) {
            return new GetDetail(
                result.brandId(),
                result.brandName(),
                result.totalProductCount()
            );
        }
    }

}
