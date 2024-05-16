package com.pedidos.service.domain.exception;

public final class PedidoNotFoundException extends GestaoDePedidosApplicationException {
    public PedidoNotFoundException(String produto) {
        super(String.format("Pedido %s não foi encontrado", produto));
    }
}