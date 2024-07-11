package com.usuarioservice.controller.exception.modal;

import org.springframework.http.HttpStatus;

import java.util.Calendar;

public final class RegistroJaExisteException extends CustomException {
    public RegistroJaExisteException(String registro) {
        this.timestamp = Calendar.getInstance().getTime();
        this.code = HttpStatus.CONFLICT.value();
        this.message = String.format("O registro [%s] que você está tentando criar já existe na base de dados.", registro);
    }
}
