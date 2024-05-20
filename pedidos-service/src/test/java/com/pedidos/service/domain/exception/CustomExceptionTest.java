package com.pedidos.service.domain.exception;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

class CustomExceptionTest {

    @Test
    void testCustomExceptionProperties() {
        Date expectedTimestamp = new Date();
        int expectedCode = 400;
        String expectedMessage = "Bad Request";
        List<String> expectedDetails = List.of("Detail1", "Detail2");
        String expectedPath = "/some/path";

        CustomException exception = mock(CustomException.class, CALLS_REAL_METHODS);
        exception.timestamp = expectedTimestamp;
        exception.code = expectedCode;
        exception.message = expectedMessage;
        exception.setDetails(expectedDetails);
        exception.setPath(expectedPath);

        assertEquals(expectedTimestamp, exception.getTimestamp());
        assertEquals(expectedCode, exception.getCode());
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedDetails, exception.getDetails());
        assertEquals(expectedPath, exception.getPath());
    }
}