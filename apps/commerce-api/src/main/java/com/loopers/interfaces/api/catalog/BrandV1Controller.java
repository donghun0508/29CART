package com.loopers.interfaces.api.catalog;

import com.loopers.application.catalog.BrandFacade;
import com.loopers.application.catalog.BrandResult.BrandDetailResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.catalog.BrandV1Dto.GetDetail;
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
    public ApiResponse<GetDetail> getBrandDetail(@PathVariable Long brandId) {
        BrandDetailResult result = brandFacade.getBrandDetail(brandId);
        return ApiResponse.success(GetDetail.from(result));
    }
}
