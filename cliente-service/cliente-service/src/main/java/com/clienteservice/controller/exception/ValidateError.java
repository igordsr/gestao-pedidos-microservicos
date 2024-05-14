package com.clienteservice.controller.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidateError extends ClienteServiceApplicationError {
    private List<ValidationMessage> messages = new ArrayList<>();

    public void addMensagens(String campo, String mensagem) {
        this.messages.add(new ValidationMessage(campo, mensagem));
    }

    public List<ValidationMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}