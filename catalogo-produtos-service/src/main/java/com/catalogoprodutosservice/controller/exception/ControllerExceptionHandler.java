package com.catalogoprodutosservice.controller.exception;

import com.catalogoprodutosservice.controller.exception.modal.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<CustomException> validationRegistroNaoEncontradoException(RegistroNaoEncontradoException err, HttpServletRequest httpServletRequest) {
        return this.toCustomException(err, httpServletRequest);
    }

    @ExceptionHandler(RegistroJaExisteException.class)
    public ResponseEntity<CustomException> validationRegistroJaExisteException(RegistroJaExisteException err, HttpServletRequest httpServletRequest) {
        return this.toCustomException(err, httpServletRequest);
    }

    @ExceptionHandler(EntidadeNaoProcessavelException.class)
    public ResponseEntity<CustomException> validationEntidadeNaoProcessavelException(EntidadeNaoProcessavelException err, HttpServletRequest httpServletRequest) {
        return this.toCustomException(err, httpServletRequest);
    }

    @ExceptionHandler(SolicitacaoInvalidaException.class)
    public ResponseEntity<CustomException> validationSolicitacaoInvalidaException(SolicitacaoInvalidaException err, HttpServletRequest httpServletRequest) {
        return this.toCustomException(err, httpServletRequest);
    }

    @ExceptionHandler(ComunicacaoApiException.class)
    public ResponseEntity<CustomException> validationRegistroNaoEncontradoException(ComunicacaoApiException err, HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(err.getCode()).body(err);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomException> validationMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest httpServletRequest) {
        final List<String> mensagens = new ArrayList<>();
        final SolicitacaoInvalidaException err = new SolicitacaoInvalidaException();
        for (FieldError f : exception.getBindingResult().getFieldErrors()) {
            String format = String.format("[%s] - %s", f.getField(), f.getDefaultMessage());
            mensagens.add(format);
        }
        err.setDetails(mensagens);
        return this.toCustomException(err, httpServletRequest);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<CustomException> validationHttpClientErrorException(HttpClientErrorException exception, HttpServletRequest httpServletRequest) {
        final List<String> mensagens = new ArrayList<>();
        final SolicitacaoInvalidaException err = new SolicitacaoInvalidaException();
        err.setDetails(mensagens);
        return this.toCustomException(err, httpServletRequest);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomException> validationDataIntegrityViolationException(DataIntegrityViolationException exception, WebRequest request) {
        EntidadeNaoProcessavelException err = new EntidadeNaoProcessavelException();
        return ResponseEntity.status(err.getCode()).body(err);
    }


    private ResponseEntity<CustomException> toCustomException(CustomException err, HttpServletRequest httpServletRequest) {
        err.setPath(httpServletRequest.getRequestURI());
        return ResponseEntity.status(err.getCode()).body(err);
    }


    private void handleHttpClientError(HttpClientErrorException e, HttpServletResponse response) throws IOException {
        String errorMessage = e.getMessage() != null ? e.getMessage() : "Erro desconhecido";
        HttpStatus statusCode = (HttpStatus) e.getStatusCode();

        switch (statusCode) {
            case UNAUTHORIZED:
                log.error("Falha na autenticação: {}", errorMessage);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Falha na autenticação: o token fornecido é inválido ou expirado");
                break;
            case FORBIDDEN:
                log.error("Acesso proibido: {}", errorMessage);
                response.sendError(HttpStatus.FORBIDDEN.value(), "Falha na autenticação: o token fornecido é inválido ou expirado");
                break;
            case BAD_REQUEST:
                log.error("Requisição inválida: {}", errorMessage);
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Requisição inválida: verifique os dados enviados.");
                break;
            case NOT_FOUND:
                log.error("Recurso não encontrado: {}", errorMessage);
                response.sendError(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado: verifique a URL.");
                break;
            default:
                log.error("Erro na requisição: {}", errorMessage);
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Erro na requisição: verifique os dados enviados.");
                break;
        }
    }
}