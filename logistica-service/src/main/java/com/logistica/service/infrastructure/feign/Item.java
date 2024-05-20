package src.main.java.com.logistica.service.infrastructure.feign;

import java.util.UUID;

public record Item(
        UUID produto,
        Integer quantidade
) {
}
