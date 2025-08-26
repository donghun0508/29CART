package com.loopers.config;

import com.loopers.resilience.config.Resilience4jConfig;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "external.payment")
public class PaymentProperties {

    private String company;
    private Map<String, ProviderConfig> providers = new HashMap<>();

    public ProviderConfig getProvider(String providerName) {
        return providers.get(providerName);
    }

    @Data
    public static class ProviderConfig {

        private Long syncInterval;
        private Long initialDelay;
        private String baseUrl;
        private String callbackUrl;
        private String secretKey;
        private String apiKey;
        private Connection connection = new Connection();
        private Executor executor = new Executor();
        private Map<String, OperationConfig> operations = new HashMap<>();

        public OperationConfig getOperationConfig(String operationName) {
            return operations.get(operationName);
        }

        @Data
        public static class Connection {

            private Timeout timeout = new Timeout();
            private Pool pool = new Pool();

            @Data
            public static class Timeout {

                private int connect = 3000;
                private int read = 10000;
            }

            @Data
            public static class Pool {

                private int maxSize = 10;
                private long idleTimeout = 30000;
            }
        }

        @Data
        public static class Executor {

            private int corePoolSize = 10;
            private int maxPoolSize = 50;
            private int queueCapacity = 100;
            private String threadNamePrefix = "payment-api-task-executor-";
            private int keepAliveSeconds = 300;
            private boolean allowCoreThreadTimeout = true;
            private boolean waitForTasksToCompleteOnShutdown = true;
            private int awaitTerminationSeconds = 60;
        }
    }

    @Data
    public static class OperationConfig {

        private String endpoint;
        private String method;
        private Resilience4jConfig resilience4j = new Resilience4jConfig();
    }
}
