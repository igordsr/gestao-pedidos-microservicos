package com.clienteservice.controller;

import com.clienteservice.controller.exception.ControllerExceptionHandler;
import com.clienteservice.controller.exception.modal.CustomException;
import com.clienteservice.controller.exception.modal.RegistroJaExisteException;
import com.clienteservice.controller.exception.modal.RegistroNaoEncontradoException;
import com.clienteservice.controller.exception.modal.SolicitacaoInvalidaException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerExceptionHandlerTest {

    @Test
    void testeRegistroNaoEncontradoException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        RegistroNaoEncontradoException exception = new RegistroNaoEncontradoException(UUID.fromString("f29c8527-c589-478b-9d7a-8ead4edbd71b").toString());
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");
        ResponseEntity<CustomException> response = handler.validationRegistroNaoEncontradoException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("O registro [f29c8527-c589-478b-9d7a-8ead4edbd71b] não foi encontrado encontrado.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void testeClienteAlreadyExistsException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        RegistroJaExisteException exception = new RegistroJaExisteException("Fulano");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");
        ResponseEntity<CustomException> response = handler.validationRegistroJaExisteException(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("O registro [Fulano] que você está tentando criar já existe na base de dados.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void testValidation() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getMessage()).thenReturn("Erro de validação");
        when(request.getRequestURI()).thenReturn("/api/resource");
        when(exception.getBindingResult()).thenReturn(bindingResult);

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "fieldName", "errorMessage"));
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<CustomException> response = handler.validationMethodArgumentNotValidException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Os parâmetros fornecidos na solicitação são inválidos ou estão ausentes. Verifique os dados e tente novamente.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void testInvalidBusinessRules() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        SolicitacaoInvalidaException exception = new SolicitacaoInvalidaException("Erro de regra de negócio");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/api/resource");

        ResponseEntity<CustomException> response = handler.validationSolicitacaoInvalidaException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void testDataIntegrityViolationException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Mensagem de erro");
        WebRequest request = mock(WebRequest.class);

        when(request.getDescription(false)).thenReturn("/api/resource");

        ResponseEntity<CustomException> response = handler.validationDataIntegrityViolationException(exception, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("A solicitação não pôde ser processada devido a dados inválidos ou à violação das regras de negócio.", Objects.requireNonNull(response.getBody()).getMessage());
    }
}