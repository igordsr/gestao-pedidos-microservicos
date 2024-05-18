package com.catalogoprodutosservice.controller.exception.modal;

import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.List;

public final class SolicitacaoInvalidaException extends CustomException {
    public SolicitacaoInvalidaException(String... detalhes) {
        this.timestamp = Calendar.getInstance().getTime();
        this.code = HttpStatus.BAD_REQUEST.value();
        this.message = "Os parâmetros fornecidos na solicitação são inválidos ou estão ausentes. Verifique os dados e tente novamente.";
        this.setDetails(List.of(detalhes));
    }
}
