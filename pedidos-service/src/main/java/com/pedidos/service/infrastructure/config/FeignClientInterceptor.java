package com.pedidos.service.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        final String currentToken = SecurityUtils.getCurrentToken();
        requestTemplate.header("Authorization", currentToken);
    }
}
