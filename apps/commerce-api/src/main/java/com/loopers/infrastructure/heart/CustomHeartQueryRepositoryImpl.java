package com.loopers.infrastructure.heart;

import static com.loopers.domain.command.heart.QHeart.heart;
import static com.loopers.domain.command.product.QBrand.brand;
import static com.loopers.domain.command.product.QProduct.product;
import static com.loopers.domain.command.user.QUser.user;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.dsl.Wildcard.count;
import static com.querydsl.jpa.JPAExpressions.select;

import com.loopers.domain.command.heart.TargetType;
import com.loopers.domain.command.user.QUser;
import com.loopers.domain.query.heart.HeartQuery;
import com.loopers.domain.query.heart.HeartQuery.List.Condition;
import com.loopers.domain.query.heart.HeartQuery.List.Response;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class CustomHeartQueryRepositoryImpl implements CustomHeartQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HeartQuery.List.Response> getHeartList(Condition condition, Pageable pageable) {
        List<Response> content = queryFactory
            .select(constructor(HeartQuery.List.Response.class
                , constructor(HeartQuery.List.Response.ProductInfo.class
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
            .from(heart)
            .join(user).on(heart.userId.eq(user.id))
            .join(product).on(heart.target.targetId.eq(product.id).and(heart.target.targetType.eq(TargetType.PRODUCT)))
            .join(brand).on(product.brand.id.eq(brand.id))
            .where(
                user.userId.eq(condition.userId())
            )
            .orderBy(heart.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(count)
            .from(heart)
            .join(user).on(heart.userId.eq(user.id))
            .where(
                user.userId.eq(condition.userId())
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
