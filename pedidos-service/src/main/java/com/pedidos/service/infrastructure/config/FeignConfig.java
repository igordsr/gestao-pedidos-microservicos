package com.pedidos.service.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    private final ObjectMapper objectMapper;

    public FeignConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    public JacksonDecoder feignDecoder() {
        return new JacksonDecoder(objectMapper);
    }

    @Bean
    public JacksonEncoder feignEncoder() {
        return new JacksonEncoder(objectMapper);
    }
}
