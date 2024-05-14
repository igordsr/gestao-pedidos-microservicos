package com.catalogoprodutosservice.controller.exception;

import com.catalogoprodutosservice.controller.exception.model.CatalogoProdutosServiceApplicationException;
import com.catalogoprodutosservice.controller.exception.model.ProdutoAlreadyExistsException;
import com.catalogoprodutosservice.controller.exception.model.ProdutoInsuficienteException;
import com.catalogoprodutosservice.controller.exception.model.ProdutoNotFoundException;
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
    private CatalogoProdutosServiceApplicationError catalogoProdutosServiceApplicationError = new CatalogoProdutosServiceApplicationError();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CatalogoProdutosServiceApplicationError> validationCatalogoProdutosServiceApplicationException(MethodArgumentNotValidException err, HttpServletRequest httpServletRequest) {
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

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<CatalogoProdutosServiceApplicationError> validationProdutoNotFoundException(ProdutoNotFoundException err, HttpServletRequest httpServletRequest) {
        return this.mapBussinesRule(err, httpServletRequest, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ProdutoAlreadyExistsException.class)
    public ResponseEntity<CatalogoProdutosServiceApplicationError> validationProdutoAlreadyExistsException(ProdutoAlreadyExistsException err, HttpServletRequest httpServletRequest) {
        return this.mapBussinesRule(err, httpServletRequest, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProdutoInsuficienteException.class)
    public ResponseEntity<CatalogoProdutosServiceApplicationError> validationProdutoInsuficienteException(ProdutoInsuficienteException err, HttpServletRequest httpServletRequest) {
        return this.mapBussinesRule(err, httpServletRequest, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(CatalogoProdutosServiceApplicationException.class)
    public ResponseEntity<CatalogoProdutosServiceApplicationError> validationCatalogoProdutosServiceApplicationException(CatalogoProdutosServiceApplicationException err, HttpServletRequest httpServletRequest) {
        return this.mapBussinesRule(err, httpServletRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CatalogoProdutosServiceApplicationError> validationCatalogoProdutosServiceApplicationException(DataIntegrityViolationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = "Ocorreu um erro ao processar a solicitação.";

        CatalogoProdutosServiceApplicationError standardError = new CatalogoProdutosServiceApplicationError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.CONFLICT.value());
        standardError.setError("Já existe um registro com os mesmos dados. Por favor, verifique os dados e tente novamente.");
        standardError.setMessage(errorMessage);
        standardError.setPath(request.getDescription(false));

        return ResponseEntity.status(status).body(standardError);
    }

    private ResponseEntity<CatalogoProdutosServiceApplicationError> mapBussinesRule(final CatalogoProdutosServiceApplicationException exception, final HttpServletRequest httpServletRequest, final HttpStatus code) {
        catalogoProdutosServiceApplicationError.setTimestamp(Instant.now());
        catalogoProdutosServiceApplicationError.setStatus(code.value());
        catalogoProdutosServiceApplicationError.setMessage(exception.getMessage());
        catalogoProdutosServiceApplicationError.setPath(httpServletRequest.getRequestURI());
        return ResponseEntity.status(code).body(this.catalogoProdutosServiceApplicationError);
    }
}