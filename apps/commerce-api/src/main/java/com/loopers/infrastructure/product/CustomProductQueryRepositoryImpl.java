package com.loopers.infrastructure.product;

import static com.loopers.domain.command.heart.QHeart.heart;
import static com.loopers.domain.command.product.QBrand.brand;
import static com.loopers.domain.command.product.QProduct.product;
import static com.loopers.domain.command.user.QUser.user;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.dsl.Expressions.FALSE;
import static com.querydsl.core.types.dsl.Wildcard.count;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static java.util.Objects.nonNull;

import com.loopers.domain.command.heart.TargetType;
import com.loopers.domain.query.product.ProductQuery;
import com.loopers.domain.query.product.ProductQuery.List.Condition;
import com.loopers.domain.query.product.ProductQuery.List.Response;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Repository
class CustomProductQueryRepositoryImpl implements CustomProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductQuery.List.Response> findProducts(Pageable pageable) {
        return findProducts(Condition.ofDefault(), pageable);
    }

    @Override
    public Page<ProductQuery.List.Response> findProducts(ProductQuery.List.Condition condition, Pageable pageable) {
        List<ProductQuery.List.Response> content = queryFactory
            .select(constructor(Response.class
                , product.id
                , constructor(Response.ProductInfo.class
                    , product.name
                    , product.stock.count.eq(0L)
                    , product.price.value
                    , brand.id
                    , brand.name
                    , select(heart.count())
                        .from(heart)
                        .where(
                            heart.target.targetId.eq(product.id)
                            , heart.target.targetType.eq(TargetType.PRODUCT)
                        )
                )
            ))
            .from(product)
            .join(brand).on(product.brand.id.eq(brand.id))
            .where(
                isProductActive()
                , eqBrandId(condition.brandId())
            )
            .orderBy(getOrderBy(condition.sort()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(count)
            .from(product);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<ProductQuery.Detail.Response> findProductDetail(ProductQuery.Detail.Condition condition) {
        return Optional.ofNullable(
            queryFactory
                .select(constructor(ProductQuery.Detail.Response.class
                    , product.id
                    , constructor(ProductQuery.Detail.Response.ProductDetailInfo.class
                        , product.name
                        , product.stock.count.eq(0L)
                        , product.price.value
                        , select(heart.count())
                            .from(heart)
                            .where(
                                heart.target.targetId.eq(product.id)
                                , heart.target.targetType.eq(TargetType.PRODUCT)
                            )
                        , selectFrom(heart)
                            .where(
                                heart.target.targetId.eq(product.id),
                                heart.target.targetType.eq(TargetType.PRODUCT),
                                condition.userId() != null ?
                                    heart.userId.eq(
                                        select(user.id)
                                            .from(user)
                                            .where(eqUserLoginId(condition.userId()))
                                    ) : FALSE
                            )
                            .exists()
                    )
                    , constructor(ProductQuery.Detail.Response.BrandDetailInfo.class
                        , brand.id
                        , brand.name
                    )
                ))
                .from(product)
                .join(brand).on(product.brand.id.eq(brand.id))
                .where(
                    isProductActive()
                    , eqProductId(condition.productId())
                )
                .fetchOne()
        );
    }

    private BooleanExpression isProductActive() {
        return product.deletedAt.isNull();
    }

    private BooleanExpression eqBrandId(Long brandId) {
        return nonNull(brandId) ? brand.id.eq(brandId) : null;
    }

    private BooleanExpression eqProductId(Long productId) {
        return nonNull(productId) ? product.id.eq(productId) : null;
    }

    private BooleanExpression eqUserLoginId(String userId) {
        return StringUtils.hasText(userId) ? user.userId.eq(userId) : null;
    }

    private OrderSpecifier<?>[] getOrderBy(Condition.SortType sortType) {
        return switch (sortType) {
            case LATEST -> new OrderSpecifier[]{product.createdAt.desc()};
            case PRICE_ASC -> new OrderSpecifier[]{product.price.value.asc()};
            case LIKES_DESC -> new OrderSpecifier[]{createLikeCountOrderDesc()};
        };
    }

    private OrderSpecifier<Long> createLikeCountOrderDesc() {
        return new OrderSpecifier<>(
            Order.DESC,
            select(heart.count())
                .from(heart)
                .where(
                    heart.target.targetId.eq(product.id),
                    heart.target.targetType.eq(TargetType.PRODUCT)
                )
        );
    }
}
