package com.clienteservice.controller.exception;

import com.clienteservice.controller.exception.model.ClienteAlreadyExistsException;
import com.clienteservice.controller.exception.model.ClienteNotFoundException;
import com.clienteservice.controller.exception.model.ClienteServiceApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {
    private ClienteServiceApplicationError clienteServiceApplicationError = new ClienteServiceApplicationError();

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ClienteServiceApplicationError> validationClienteNotFoundException(ClienteNotFoundException err, HttpServletRequest httpServletRequest) {
        HttpStatus code = HttpStatus.NOT_FOUND;
        clienteServiceApplicationError.setTimestamp(Instant.now());
        clienteServiceApplicationError.setStatus(code.value());
        clienteServiceApplicationError.setError("Entity not Found");
        clienteServiceApplicationError.setMessage(err.getMessage());
        clienteServiceApplicationError.setPath(httpServletRequest.getRequestURI());

        return ResponseEntity.status(code).body(this.clienteServiceApplicationError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClienteServiceApplicationError> validation(MethodArgumentNotValidException err, HttpServletRequest httpServletRequest) {
        HttpStatus code = HttpStatus.BAD_REQUEST;
        ValidateError validateError = new ValidateError();
        validateError.setTimestamp(Instant.now());
        validateError.setStatus(code.value());
        validateError.setError("Erro de Validação");
        validateError.setMessage(err.getMessage());
        validateError.setPath(httpServletRequest.getRequestURI());
        for (FieldError f : err.getBindingResult().getFieldErrors()) {
            validateError.addMensagens(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(code).body(validateError);
    }

    @ExceptionHandler(ClienteAlreadyExistsException.class)
    public ResponseEntity<ClienteServiceApplicationError> validationClienteAlreadyExistsException(ClienteAlreadyExistsException err, HttpServletRequest httpServletRequest) {
        HttpStatus code = HttpStatus.CONFLICT;
        clienteServiceApplicationError.setTimestamp(Instant.now());
        clienteServiceApplicationError.setStatus(code.value());
        clienteServiceApplicationError.setMessage(err.getMessage());
        clienteServiceApplicationError.setPath(httpServletRequest.getRequestURI());
        return ResponseEntity.status(code).body(this.clienteServiceApplicationError);
    }

    @ExceptionHandler(ClienteServiceApplicationException.class)
    public ResponseEntity<ClienteServiceApplicationError> validation(ClienteServiceApplicationException err, HttpServletRequest httpServletRequest) {
        HttpStatus code = HttpStatus.BAD_REQUEST;
        clienteServiceApplicationError.setTimestamp(Instant.now());
        clienteServiceApplicationError.setStatus(code.value());
        clienteServiceApplicationError.setMessage(err.getMessage());
        clienteServiceApplicationError.setPath(httpServletRequest.getRequestURI());
        return ResponseEntity.status(code).body(this.clienteServiceApplicationError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ClienteServiceApplicationError> validation(DataIntegrityViolationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = "Ocorreu um erro ao processar a solicitação.";

        ClienteServiceApplicationError standardError = new ClienteServiceApplicationError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.CONFLICT.value());
        standardError.setError("Já existe um registro com os mesmos dados. Por favor, verifique os dados e tente novamente.");
        standardError.setMessage(errorMessage);
        standardError.setPath(request.getDescription(false));

        return ResponseEntity.status(status).body(standardError);
    }
}