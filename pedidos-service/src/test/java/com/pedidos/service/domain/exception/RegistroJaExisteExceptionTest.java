package com.pedidos.service.domain.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class RegistroJaExisteExceptionTest {

    @Test
    void testRegistroJaExisteException() {
        String registro = "someEntity";

        RegistroJaExisteException exception = new RegistroJaExisteException(registro);

        assertNotNull(exception.getTimestamp(), "Timestamp should not be null");
        assertEquals(HttpStatus.CONFLICT.value(), exception.getCode(), "The status code should match CONFLICT (409)");
        assertEquals(String.format("O registro [%s] que você está tentando criar já existe na base de dados.", registro), exception.getMessage(), "The message should be properly formatted with the entity name");

        long now = Calendar.getInstance().getTimeInMillis();
        long exceptionTime = exception.getTimestamp().getTime();
        assertTrue(Math.abs(now - exceptionTime) < 1000, "The timestamp should be within the current time");
    }
}