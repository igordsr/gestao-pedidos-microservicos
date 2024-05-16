package com.pedidos.service.domain.exception;

public abstract class GestaoDePedidosApplicationException extends RuntimeException {
    public GestaoDePedidosApplicationException(String message) {
        super(message.toUpperCase());
    }
}