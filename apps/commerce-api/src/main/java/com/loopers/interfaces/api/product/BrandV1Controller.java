package com.loopers.interfaces.api.product;

import com.loopers.application.product.BrandFacade;
import com.loopers.domain.query.product.BrandQuery.Detail;
import com.loopers.domain.query.product.BrandQuery.Detail.Condition;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/brands")
public class BrandV1Controller implements BrandV1ApiSpec {

    private final BrandFacade brandFacade;

    @Override
    @GetMapping("/{brandId}")
    public ApiResponse<BrandV1Dto.GetDetail.Response> getBrandDetail(@PathVariable Long brandId) {
        Condition condition = new Condition(brandId);
        Detail.Response response = brandFacade.getBrandDetail(condition);
        return ApiResponse.success(BrandV1Dto.GetDetail.Response.from(response));
    }
}
