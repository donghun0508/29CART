package com.loopers.infrastructure.client;

import com.loopers.support.error.CoreException;
import feign.FeignException;
import jakarta.validation.ValidationException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
public class PaymentRequestRetryPolicy implements Predicate<Throwable> {

    @Override
    public boolean test(Throwable throwable) {

        if (throwable instanceof IllegalStateException ||
            throwable instanceof IllegalArgumentException ||
            throwable instanceof ValidationException) {
            log.warn("비즈니스 로직 오류로 재시도하지 않음: {}", throwable.getClass().getSimpleName());
            return false;
        }

        boolean shouldRetry = throwable instanceof CoreException ||
            throwable instanceof FeignException.InternalServerError ||  // 500
            throwable instanceof FeignException.ServiceUnavailable ||   // 503
            throwable instanceof FeignException.BadGateway ||           // 502
            throwable instanceof FeignException ||  // 다른 Feign 예외들도 포함
            throwable instanceof RuntimeException ||  // RuntimeException으로 래핑된 경우
            throwable instanceof SocketTimeoutException ||
            throwable instanceof ResourceAccessException ||
            throwable instanceof ConnectException ||  // Connection refused 포함
            throwable instanceof HttpTimeoutException;

        log.warn("재시도 여부: {} (예외 타입: {}), reason: {}", shouldRetry, throwable.getClass().getSimpleName(), throwable.getMessage());
        return shouldRetry;
    }
}
