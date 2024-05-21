package com.pedidos.service.infrastructure.feign.vo;

import java.util.UUID;

public record ProdutoVO(UUID id, String nome, String descricao, Double preco, Integer qtdEstoque) {
}