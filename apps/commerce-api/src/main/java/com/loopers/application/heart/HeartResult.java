package com.loopers.application.heart;

import com.loopers.domain.heart.Heart;

public record HeartResult(
    ProductInfo product
) {

    public static HeartResult from(Heart heart) {
        return null;
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
