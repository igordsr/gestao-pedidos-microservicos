package com.pedidos.service.domain.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitacaoInvalidaExceptionTest {

    @Test
    void testSolicitacaoInvalidaException() {
        String[] detalhes = {"Campo 'nome' é obrigatório", "Campo 'idade' deve ser um número positivo"};

        SolicitacaoInvalidaException exception = new SolicitacaoInvalidaException(detalhes);

        assertNotNull(exception.getTimestamp(), "Timestamp should not be null");
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getCode(), "The status code should match BAD_REQUEST (400)");
        assertEquals("Os parâmetros fornecidos na solicitação são inválidos ou estão ausentes. Verifique os dados e tente novamente.", exception.getMessage(), "The message should be the predefined one");
        assertEquals(List.of(detalhes), exception.getDetails(), "The details should match the provided details");

        long now = Calendar.getInstance().getTimeInMillis();
        long exceptionTime = exception.getTimestamp().getTime();
        assertTrue(Math.abs(now - exceptionTime) < 1000, "The timestamp should be within the current time");
    }
}