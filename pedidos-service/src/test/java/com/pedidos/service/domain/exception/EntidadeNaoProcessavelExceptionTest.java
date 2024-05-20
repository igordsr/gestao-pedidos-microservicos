package com.pedidos.service.domain.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntidadeNaoProcessavelExceptionTest {

    @Test
    void testEntidadeNaoProcessavelException() {
        String[] expectedDetails = {"Invalid field", "Constraint violation"};

        EntidadeNaoProcessavelException exception = new EntidadeNaoProcessavelException(expectedDetails);

        assertNotNull(exception.getTimestamp(), "Timestamp should not be null");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getCode(), "The status code should match UNPROCESSABLE_ENTITY");
        assertEquals("A solicitação não pôde ser processada devido a dados inválidos ou à violação das regras de negócio.", exception.getMessage(), "The message should match the predefined one");
        assertEquals(List.of(expectedDetails), exception.getDetails(), "The details should match the given messages");

        long now = Calendar.getInstance().getTimeInMillis();
        long exceptionTime = exception.getTimestamp().getTime();
        assertTrue(Math.abs(now - exceptionTime) < 1000, "The timestamp should be within the current time");
    }
}