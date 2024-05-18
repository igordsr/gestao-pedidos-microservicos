package com.clienteservice.controller.exception.modal;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ComunicacaoApiException extends CustomException {

    public ComunicacaoApiException(int code, String message, String path) {
        this.code = code;
        this.message = message;
        this.path = path;
    }
}
