package com.clienteservice.controller.exception.model;

public final class ClienteAlreadyExistsException extends ClienteServiceApplicationException {
    public ClienteAlreadyExistsException(final String nome) {
        super(String.format("Cliente %s já está cadastrado no sistema.", nome.toUpperCase()));
    }
}
