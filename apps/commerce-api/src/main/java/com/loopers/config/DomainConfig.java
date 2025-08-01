package com.loopers.config;

import com.loopers.domain.command.order.OrderFactory;
import com.loopers.domain.command.product.StockManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public StockManager stockManager() {
        return new StockManager();
    }

    @Bean
    public OrderFactory orderFactory() {
        return new OrderFactory();
    }

}
