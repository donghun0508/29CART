package com.loopers.application.catalog;

import com.loopers.application.catalog.ProductResult.ProductDetailResult.ProductDetailInfo;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.ProductSliceRead;
import java.util.List;
import java.util.stream.IntStream;
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
    public record ProductSliceRankingResult(Long productId, int rank, ProductInfo productInfo) {

        public static Slice<ProductSliceRankingResult> from(ProductSliceRead read) {
            int startRank = read.getNumber() * read.getSize() + 1;

            List<ProductSliceRankingResult> content =
                IntStream.range(0, read.getContent().size())
                    .mapToObj(i -> {
                        ProductRead pr = read.getContent().get(i);
                        return ProductSliceRankingResult.builder()
                            .productId(pr.getId())
                            .rank(startRank + i)
                            .productInfo(new ProductInfo(
                                pr.getName(),
                                pr.isSoldOut(),
                                pr.getUnitPrice(),
                                pr.getBrandId(),
                                pr.getBrandName(),
                                pr.getHeartCount()
                            ))
                            .build();
                    })
                    .toList();

            return new SliceImpl<>(
                content,
                PageRequest.of(read.getNumber(), read.getSize()),
                read.isHasNext()
            );
        }

        @Builder(access = AccessLevel.PRIVATE)
        public record ProductInfo(
            String productName,
            boolean isSoldOut,
            Long price,
            Long brandId,
            String brandName,
            Long likeCount
        ) {}
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record ProductDetailResult(Long productId, Integer rank, ProductDetailInfo product, BrandDetailInfo brand) {

        public static ProductDetailResult from(ProductRead productRead, Integer rank) {
            return ProductDetailResult.builder()
                .productId(productRead.getId())
                .rank(rank)
                .product(ProductDetailResult.ProductDetailInfo.from(productRead, productRead.isLiked()))
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
