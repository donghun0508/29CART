package com.loopers.application.heart.validate;

import static com.loopers.domain.command.heart.TargetType.PRODUCT;

import com.loopers.domain.command.heart.TargetType;
import com.loopers.domain.command.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ProductTargetValidate implements TargetValidate {

    private final ProductService productService;

    @Override
    public boolean supports(TargetType targetType) {
        return PRODUCT.equals(targetType);
    }

    @Override
    public void validTargetId(Long targetId) {
        if (!productService.existById(targetId)) {
            throw new CoreException(ErrorType.NOT_FOUND);
        }
    }
}
