package com.pedidos.service.domain.exception;

public final class ClienteNotFoundException extends GestaoDePedidosApplicationException {
    public ClienteNotFoundException() {
        super("Cliente não foi encontrado");
    }
}