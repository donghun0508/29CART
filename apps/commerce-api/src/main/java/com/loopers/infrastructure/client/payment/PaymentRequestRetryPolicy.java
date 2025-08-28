package com.loopers.infrastructure.client.payment;

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
            return false;
        }

        boolean shouldRetry = throwable instanceof CoreException ||
            throwable instanceof FeignException.ServiceUnavailable ||
            throwable instanceof FeignException.BadGateway ||
            throwable instanceof FeignException ||
            throwable instanceof RuntimeException ||
            throwable instanceof SocketTimeoutException ||
            throwable instanceof ResourceAccessException ||
            throwable instanceof ConnectException ||
            throwable instanceof HttpTimeoutException;

        log.warn("PG 시스템 연동 중 오류가 발생하여 재시도합니다. [사유: {}]", throwable.getMessage());
        return shouldRetry;
    }
}
