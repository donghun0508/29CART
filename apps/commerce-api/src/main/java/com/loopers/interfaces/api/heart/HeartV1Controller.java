package com.loopers.interfaces.api.heart;

import com.loopers.application.heart.HeartCommand.LikeCommand;
import com.loopers.application.heart.HeartCommand.UnlikeCommand;
import com.loopers.application.heart.HeartFacade;
import com.loopers.application.heart.HeartResult;
import com.loopers.domain.heart.TargetType;
import com.loopers.interfaces.api.ApiHeaders;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PaginationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/like")
public class HeartV1Controller implements HeartV1ApiSpec {

    private final HeartFacade heartFacade;

    @Override
    @PostMapping("/products/{productId}")
    public ApiResponse<?> addHeart(
        @PathVariable Long productId,
        @RequestHeader(value = ApiHeaders.USER_ID, required = true) String userId
    ) {
        LikeCommand criteria = LikeCommand.of(userId, productId, TargetType.PRODUCT);
        heartFacade.heart(criteria);
        return ApiResponse.success();
    }

    @Override
    @DeleteMapping("/products/{productId}")
    public ApiResponse<?> removeHeart(
        @PathVariable Long productId,
        @RequestHeader(value = ApiHeaders.USER_ID, required = true) String userId
    ) {
        UnlikeCommand criteria = UnlikeCommand.of(userId, productId, TargetType.PRODUCT);
        heartFacade.unHeart(criteria);
        return ApiResponse.success();
    }

    @Override
    @GetMapping("/products")
    public ApiResponse<Page<HeartV1Dto.Response>> getHeartList(
        @ModelAttribute PaginationRequest paginationRequest,
        @RequestHeader(value = ApiHeaders.USER_ID, required = true) String userId
    ) {
        Page<HeartResult> heartResults = heartFacade.getHeartList(userId, paginationRequest.toPageable());
        return ApiResponse.success(heartResults.map(HeartV1Dto.Response::from));
    }
}
