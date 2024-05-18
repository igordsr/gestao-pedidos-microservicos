package com.clienteservice.controller.exception.modal;

import org.springframework.http.HttpStatus;

import java.util.Calendar;

public final class RegistroNaoEncontradoException extends CustomException {
    public RegistroNaoEncontradoException(String registro) {
        this.timestamp = Calendar.getInstance().getTime();
        this.code = HttpStatus.NOT_FOUND.value();
        this.message = String.format("O registro [%s] não foi encontrado encontrado.", registro);
    }
}
