package com.clienteservice.controller.exception.model;

public final class ClienteNotFoundException extends ClienteServiceApplicationException {
    public ClienteNotFoundException() {
        super("Cliente não foi encontrado");
    }
}