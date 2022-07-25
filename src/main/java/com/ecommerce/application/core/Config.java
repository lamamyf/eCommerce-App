package com.ecommerce.application.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

@Configuration
public class Config {

    @Bean
    public SpelAwareProxyProjectionFactory spelAwareProxyProjectionFactory(){
        return new SpelAwareProxyProjectionFactory();
    }
}
