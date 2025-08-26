package com.loopers.config;

import static com.loopers.config.PaymentConfig.SimulatorProviderConfig.OperationConfig.OperationPaymentRequestConfig.Constants.CIRCUIT_PAYMENT;
import static com.loopers.config.PaymentConfig.SimulatorProviderConfig.OperationConfig.OperationPaymentRequestConfig.Constants.OPERATION_PAYMENT_REQUEST;
import static com.loopers.config.PaymentConfig.SimulatorProviderConfig.OperationConfig.OperationPaymentRequestConfig.Constants.RETRY_PAYMENT;

import com.loopers.infrastructure.client.PaymentRequestRetryPolicy;
import com.loopers.resilience.Resilience4jConfigFactory;
import feign.RequestInterceptor;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@EnableConfigurationProperties(PaymentProperties.class)
public class PaymentConfig {

    private PaymentConfig() {

    }

    @Configuration
    public static class SimulatorProviderConfig {

        public static final String SIMULATOR_PROVIDER_NAME = "simulator";

        @Bean("paymentHttpClient")
        public OkHttpClient paymentHttpClient(PaymentProperties properties, Environment environment) {
            var connection = properties.getProvider(SIMULATOR_PROVIDER_NAME).getConnection();
            var timeout = connection.getTimeout();
            var pool = connection.getPool();

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMillis(timeout.getConnect()))
                .readTimeout(Duration.ofMillis(timeout.getRead()))
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(
                    pool.getMaxSize(),
                    pool.getIdleTimeout(),
                    TimeUnit.MILLISECONDS
                ));

            if (environment.acceptsProfiles(Profiles.of("local"))) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logging);
            }

            return builder.build();
        }

        @Bean
        public RequestInterceptor requestInterceptor(PaymentProperties properties) {
            return template -> template.header("X-USER-ID", properties.getCompany());
        }

        @Bean("paymentRequestTaskExecutor")
        public ThreadPoolTaskExecutor paymentRequestTaskExecutor(PaymentProperties paymentProperties) {
            var providerConfig = paymentProperties.getProvider(SIMULATOR_PROVIDER_NAME);
            var executorConfig = providerConfig.getExecutor();

            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(executorConfig.getCorePoolSize());
            executor.setMaxPoolSize(executorConfig.getMaxPoolSize());
            executor.setQueueCapacity(executorConfig.getQueueCapacity());
            executor.setThreadNamePrefix(executorConfig.getThreadNamePrefix());
            executor.setKeepAliveSeconds(executorConfig.getKeepAliveSeconds());
            executor.setAllowCoreThreadTimeOut(executorConfig.isAllowCoreThreadTimeout());
            executor.setWaitForTasksToCompleteOnShutdown(executorConfig.isWaitForTasksToCompleteOnShutdown());
            executor.setAwaitTerminationSeconds(executorConfig.getAwaitTerminationSeconds());
            executor.initialize();
            return executor;
        }

        @Configuration
        @NoArgsConstructor
        public static class OperationConfig {

            @Configuration
            @RequiredArgsConstructor
            public static class OperationPaymentRequestConfig {

                private final PaymentProperties paymentProperties;
                private final Resilience4jConfigFactory configFactory;

                public static class Constants {

                    public static final String CIRCUIT_PAYMENT = "CB_PAYMENT_REQUEST";
                    public static final String RETRY_PAYMENT = "RETRY_PAYMENT_REQUEST";
                    public static final String OPERATION_PAYMENT_REQUEST = "payment-request";

                    private Constants() {

                    }
                }

                @Bean
                public CircuitBreaker paymentCircuitBreaker() {
                    var operationConfig = paymentProperties.getProvider(SIMULATOR_PROVIDER_NAME)
                        .getOperationConfig(OPERATION_PAYMENT_REQUEST);
                    var cbConfig = operationConfig.getResilience4j().getCircuitbreaker();

                    return configFactory.createCircuitBreaker(CIRCUIT_PAYMENT, cbConfig);
                }

                @Bean
                public Retry paymentRetry() {
                    var operationConfig = paymentProperties.getProvider(SIMULATOR_PROVIDER_NAME)
                        .getOperationConfig(OPERATION_PAYMENT_REQUEST);
                    var retryConfig = operationConfig.getResilience4j().getRetry();

                    return configFactory.createRetry(RETRY_PAYMENT, retryConfig,
                        Boolean.TRUE.equals(retryConfig.getManual()) ? new PaymentRequestRetryPolicy() : null);
                }
            }
        }
    }
}
