package com.loopers.infrastructure.product;

import static com.loopers.domain.command.product.QBrand.brand;
import static com.loopers.domain.command.product.QProduct.product;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.jpa.JPAExpressions.select;

import com.loopers.domain.query.product.BrandQuery;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class CustomBrandQueryRepositoryImpl implements CustomBrandQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<BrandQuery.Detail.Response> findBrandDetail(BrandQuery.Detail.Condition condition) {
        return Optional.ofNullable(
            queryFactory
                .select(constructor(BrandQuery.Detail.Response.class
                        , brand.id
                        , brand.name
                        , select(product.count())
                            .from(product)
                            .where(
                                product.brand.id.eq(brand.id)
                            )
                    )
                )
                .from(brand)
                .where(
                    isBrandActive(),
                    eqBrandId(condition.brandId())
                )
                .fetchOne()
        );
    }

    private BooleanExpression isBrandActive() {
        return brand.deletedAt.isNull();
    }

    private BooleanExpression eqBrandId(Long brandId) {
        return brand.id.eq(brandId);
    }
}
