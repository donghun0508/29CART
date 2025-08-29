package com.loopers.application.heart.processor;

import static com.loopers.domain.heart.TargetType.PRODUCT;

import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.heart.TargetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductTargetProcess implements TargetProcess {

    private final ProductService productService;

    @Override
    public boolean supports(TargetType targetType) {
        return PRODUCT.equals(targetType);
    }

    @Override
    public void acquireTargetLock(Long targetId) {
        productService.findByIdWithLock(targetId);
    }
}
