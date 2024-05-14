package com.catalogoprodutosservice.controller.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidationMessage {
    private String campo;
    private String mensagem;

    public ValidationMessage(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}