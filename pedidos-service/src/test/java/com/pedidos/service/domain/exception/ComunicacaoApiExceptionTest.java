package com.pedidos.service.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComunicacaoApiExceptionTest {

    @Test
    void testComunicacaoApiException() {
        int expectedCode = 404;
        String expectedMessage = "Recurso não encontrado";
        String expectedPath = "/algum/recurso";

        ComunicacaoApiException exception = new ComunicacaoApiException(expectedCode, expectedMessage, expectedPath);

        assertEquals(expectedCode, exception.getCode());
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedPath, exception.getPath());
    }
}