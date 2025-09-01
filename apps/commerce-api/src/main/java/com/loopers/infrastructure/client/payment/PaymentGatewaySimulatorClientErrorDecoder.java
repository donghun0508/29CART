package com.loopers.infrastructure.client.payment;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class PaymentGatewaySimulatorClientErrorDecoder implements ErrorDecoder {

    // decoder -> retryPredicate -> fallbackmethod
    // ErrorDecoder는 오직 HTTP 응답이 있을 때만 동작합니다.
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == NOT_FOUND.value() ||
            response.status() < 400 ||
            response.status() >= 600) {
            return new Default().decode(methodKey, response);
        }

        String errorMessage = extractErrorMessage(response);

        if (response.status() < 500) {
            return new IllegalArgumentException(errorMessage);
        }

        return new CoreException(ErrorType.INTERNAL_ERROR, errorMessage);
    }

    private String extractErrorMessage(Response response) {
        try {
            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(body);

            String message = jsonNode.path("meta").path("message").asText();
            if (!message.isEmpty()) {
                return message;
            }

            if (response.status() >= 500) {
                return "PG 서버 내부 오류: " + response.status();
            }
            return "잘못된 요청: " + response.status();

        } catch (Exception e) {
            if (response.status() >= 500) {
                return "PG 서버 내부 오류: " + response.status();
            }
            return "잘못된 요청: " + response.status();
        }
    }
}
