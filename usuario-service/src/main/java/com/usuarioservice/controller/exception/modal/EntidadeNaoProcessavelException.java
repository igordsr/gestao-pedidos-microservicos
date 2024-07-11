package com.usuarioservice.controller.exception.modal;

import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.List;

public final class EntidadeNaoProcessavelException extends CustomException {
    public EntidadeNaoProcessavelException(String... mensagens) {
        this.timestamp = Calendar.getInstance().getTime();
        this.code = HttpStatus.UNPROCESSABLE_ENTITY.value();
        this.message = "A solicitação não pôde ser processada devido a dados inválidos ou à violação das regras de negócio.";
        this.details = List.of(mensagens);
    }
}
