package com.pedidos.service.domain.exception;

public final class ProdutoInsuficienteException extends GestaoDePedidosApplicationException {
    public ProdutoInsuficienteException(final String produto) {
        super(String.format("Não há unidades suficientes do produto %S para a demanda solicitada", produto));
    }
}
