package com.loopers.application.product;

import com.loopers.domain.query.product.BrandQuery;
import com.loopers.domain.query.product.BrandQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BrandFacade {

    private final BrandQueryService brandQueryService;

    public BrandQuery.Detail.Response getBrandDetail(BrandQuery.Detail.Condition condition) {
        return brandQueryService.findBrandDetail(condition);
    }
}
