package com.pedidos.service.domain.exception;

public final class ProdutoNotFoundException extends GestaoDePedidosApplicationException {
    public ProdutoNotFoundException(String produto) {
        super(String.format("Produto %s não foi encontrado", produto));
    }
}