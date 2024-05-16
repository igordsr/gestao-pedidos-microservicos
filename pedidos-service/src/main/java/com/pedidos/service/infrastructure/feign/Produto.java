package com.pedidos.service.infrastructure.feign;

import java.util.UUID;

public record Produto(UUID id, String nome, String descricao, Double preco, Integer qtdEstoque) {
}