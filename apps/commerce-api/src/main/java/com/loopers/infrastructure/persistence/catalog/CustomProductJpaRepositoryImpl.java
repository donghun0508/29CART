package com.loopers.infrastructure.persistence.catalog;

import static com.loopers.domain.catalog.QBrand.brand;
import static com.loopers.domain.catalog.QProduct.product;
import static com.loopers.domain.catalog.SortType.LIKES_DESC;
import static com.loopers.domain.catalog.SortType.PRICE_ASC;
import static java.util.Objects.nonNull;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.SortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class CustomProductJpaRepositoryImpl implements CustomProductJpaRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Product> findSliceByBrandId(ListCondition condition) {
        List<Product> content = queryFactory
            .select(product)
            .from(product)
            .where(
                eqBrandId(condition.brandId())
            )
            .orderBy(getOrderBy(condition.sort()))
            .limit(condition.pageSize() + 1L)
            .offset(condition.offSet())
            .fetch();

        content.forEach(product -> {
            product.getBrand().getName();
        });

        boolean hasNext = content.size() > condition.pageSize();

        List<Product> actualContent = hasNext ?
            content.subList(0, condition.pageSize()) : content;

        return new SliceImpl<>(actualContent, condition.pageable(), hasNext);
    }

    private BooleanExpression eqBrandId(Long brandId) {
        return nonNull(brandId) ? brand.id.eq(brandId) : null;
    }

    private OrderSpecifier<?>[] getOrderBy(SortType sort) {
        if (sort.equals(LIKES_DESC)) {
            return new OrderSpecifier[]{product.heartCount.value.desc()};
        }
        if (sort.equals(PRICE_ASC)) {
            return new OrderSpecifier[]{product.unitPrice.value.asc()};
        }

        return new OrderSpecifier[]{product.createdAt.desc()};
    }
}
