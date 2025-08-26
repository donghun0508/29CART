package com.loopers.application.catalog;

import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.ProductSliceRead;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class ProductResult {

    @Builder(access = AccessLevel.PRIVATE)
    public record ProductSliceResult(Long productId, ProductInfo productInfo) {

        public static Slice<ProductSliceResult> from(ProductSliceRead productSliceRead) {
            return new SliceImpl<>(
                productSliceRead.getContent().stream().map(ProductSliceResult::from).toList(),
                PageRequest.of(productSliceRead.getNumber(), productSliceRead.getSize()),
                productSliceRead.isHasNext()
            );
        }

        private static ProductSliceResult from(ProductRead productRead) {
            return ProductSliceResult.builder()
                .productId(productRead.getId())
                .productInfo(ProductInfo.builder()
                    .productName(productRead.getName())
                    .isSoldOut(productRead.isSoldOut())
                    .price(productRead.getUnitPrice())
                    .brandId(productRead.getBrandId())
                    .brandName(productRead.getBrandName())
                    .likeCount(productRead.getHeartCount())
                    .build())
                .build();
        }

        @Builder(access = AccessLevel.PRIVATE)
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

    @Builder(access = AccessLevel.PRIVATE)
    public record ProductDetailResult(Long productId, ProductDetailInfo product, BrandDetailInfo brand) {

        public static ProductDetailResult from(ProductRead productRead) {
            return ProductDetailResult.builder()
                .productId(productRead.getId())
                .product(ProductDetailInfo.from(productRead, productRead.isLiked()))
                .brand(BrandDetailInfo.from(productRead))
                .build();
        }

        @Builder(access = AccessLevel.PRIVATE)
        public record ProductDetailInfo(
            String productName,
            boolean isSoldOut,
            Long price,
            Long likeCount,
            boolean isLiked
        ) {

            public static ProductDetailInfo from(ProductRead product, boolean isLiked) {
                return new ProductDetailInfo(
                    product.getName(),
                    product.isSoldOut(),
                    product.getUnitPrice(),
                    product.getHeartCount(),
                    isLiked
                );
            }
        }

        @Builder(access = AccessLevel.PRIVATE)
        public record BrandDetailInfo(Long brandId, String brandName) {

            public static BrandDetailInfo from(ProductRead productRead) {
                return new BrandDetailInfo(
                    productRead.getBrandId(),
                    productRead.getBrandName()
                );
            }
        }
    }
}
