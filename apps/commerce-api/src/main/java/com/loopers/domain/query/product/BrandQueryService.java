package com.loopers.domain.query.product;

import static com.loopers.support.error.ErrorType.NOT_FOUND;

import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BrandQueryService {

    private final BrandQueryRepository brandQueryRepository;

    public BrandQuery.Detail.Response findBrandDetail(BrandQuery.Detail.Condition condition) {
        return brandQueryRepository.findBrandDetail(condition)
            .orElseThrow(() -> new CoreException(NOT_FOUND));
    }

}
