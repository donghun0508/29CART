package com.loopers.feign.config;

import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.loopers")
public class FeignClientConfig {

    @Bean
    public RequestInterceptor paymentRequestInterceptor() {
        return template -> {
            template.header("Content-Type", "application/json");
            template.header("Accept", "application/json");
        };
    }
}
