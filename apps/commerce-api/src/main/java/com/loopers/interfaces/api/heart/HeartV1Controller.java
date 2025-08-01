package com.loopers.interfaces.api.heart;

import com.loopers.application.heart.HeartFacade;
import com.loopers.domain.command.heart.Target;
import com.loopers.domain.command.heart.TargetType;
import com.loopers.domain.query.heart.HeartQuery;
import com.loopers.domain.query.heart.HeartQuery.List.Condition;
import com.loopers.interfaces.api.ApiHeaders;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.heart.HeartV1Dto.GetList.Response;
import com.loopers.interfaces.api.shared.PaginationRequest;
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
        heartFacade.like(userId, Target.of(productId, TargetType.PRODUCT));
        return ApiResponse.success();
    }

    @Override
    @DeleteMapping("/products/{productId}")
    public ApiResponse<?> removeHeart(
        @PathVariable Long productId,
        @RequestHeader(value = ApiHeaders.USER_ID, required = true) String userId
    ) {
        heartFacade.unlike(userId, Target.of(productId, TargetType.PRODUCT));
        return ApiResponse.success();
    }

    @Override
    @GetMapping("/products")
    public ApiResponse<Page<Response>> getHeartList(
        @ModelAttribute PaginationRequest paginationRequest,
        @RequestHeader(value = ApiHeaders.USER_ID, required = true) String userId
    ) {
        Condition condition = new Condition(userId);
        Page<HeartQuery.List.Response> responses = heartFacade.getHeartList(condition, paginationRequest.toPageable());
        return ApiResponse.success(responses.map(HeartV1Dto.GetList.Response::from));
    }
}
