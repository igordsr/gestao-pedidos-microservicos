package com.logistica.service.infrastructure.feign.vo;

import java.util.UUID;

public record Item(
        UUID produto,
        Integer quantidade
) {
}
