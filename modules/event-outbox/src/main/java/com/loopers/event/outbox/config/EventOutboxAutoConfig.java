package com.loopers.event.outbox.config;

import static com.loopers.event.outbox.config.EventAsyncConstant.EVENT;
import static com.loopers.event.outbox.config.EventAsyncConstant.RELAY;

import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ContextSnapshotFactory;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.contextpropagation.ObservationAwareBaggageThreadLocalAccessor;
import io.micrometer.tracing.contextpropagation.ObservationAwareSpanThreadLocalAccessor;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@AutoConfiguration
@EnableScheduling
@EnableAsync
@ConditionalOnClass(TaskExecutor.class)
@EnableConfigurationProperties(ThreadPoolProperties.class)
@PropertySource(value = "classpath:event-outbox.yml", factory = YamlPropertySourceFactory.class)
public class EventOutboxAutoConfig {

    @Bean
    TaskDecorator tracingTaskDecorator() {
        var factory = ContextSnapshotFactory.builder()
            .contextRegistry(ContextRegistry.getInstance())
            .build();
        return r -> factory.captureAll().wrap(r);
    }

    @Bean(EVENT)
    @Primary
    public TaskExecutor executor(ThreadPoolProperties properties, TaskDecorator tracingTaskDecorator) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeout());
        executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        executor.setTaskDecorator(tracingTaskDecorator);
        executor.initialize();
        return executor;
    }

    @Bean(RELAY)
    public Executor outboxRelayExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Bean
    ObservationAwareBaggageThreadLocalAccessor baggageAccessor(
        ObservationRegistry observationRegistry, Tracer tracer) {
        var acc = new ObservationAwareBaggageThreadLocalAccessor(observationRegistry, tracer);
        ContextRegistry.getInstance().registerThreadLocalAccessor(acc);
        return acc;
    }

    @Bean
    ObservationAwareSpanThreadLocalAccessor spanAccessor(
        ObservationRegistry observationRegistry, Tracer tracer) {
        var acc = new ObservationAwareSpanThreadLocalAccessor(observationRegistry, tracer);
        ContextRegistry.getInstance().registerThreadLocalAccessor(acc);
        return acc;
    }
}
