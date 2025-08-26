package com.loopers.resilience.config;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@ConditionalOnClass(CircuitBreaker.class)
@PropertySource(value = "classpath:resilience.yml", factory = YamlPropertySourceFactory.class)
public class ResilienceAutoConfiguration {

}
