package com.pedidos.service.domain.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class RegistroNaoEncontradoExceptionTest {

    @Test
    void testRegistroNaoEncontradoException() {
        String registro = "TestEntity";

        RegistroNaoEncontradoException exception = new RegistroNaoEncontradoException(registro);

        assertNotNull(exception.getTimestamp(), "Timestamp should not be null");
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getCode(), "The status code should match NOT_FOUND (404)");
        assertEquals(String.format("O registro [%s] não foi encontrado.", registro), exception.getMessage(), "The message should be properly formatted with the registro");

        long now = Calendar.getInstance().getTimeInMillis();
        long exceptionTime = exception.getTimestamp().getTime();
        assertTrue(Math.abs(now - exceptionTime) < 1000, "The timestamp should be within the current time");
    }
}