package com.catalogoprodutosservice.controller;

import com.catalogoprodutosservice.controller.exception.CatalogoProdutosServiceApplicationError;
import com.catalogoprodutosservice.controller.exception.ControllerExceptionHandler;
import com.catalogoprodutosservice.controller.exception.model.CatalogoProdutosServiceApplicationException;
import com.catalogoprodutosservice.controller.exception.model.ProdutoAlreadyExistsException;
import com.catalogoprodutosservice.controller.exception.model.ProdutoInsuficienteException;
import com.catalogoprodutosservice.controller.exception.model.ProdutoNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerExceptionHandlerTest {

    @Test
    void testeProdutoNotFoundException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ProdutoNotFoundException exception = new ProdutoNotFoundException();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");
        ResponseEntity<CatalogoProdutosServiceApplicationError> response = handler.validationProdutoNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produto não foi encontrado", response.getBody().getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void testeProdutoAlreadyExistsException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ProdutoAlreadyExistsException exception = new ProdutoAlreadyExistsException("Fulano");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");
        ResponseEntity<CatalogoProdutosServiceApplicationError> response = handler.validationProdutoAlreadyExistsException(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Produto FULANO já está cadastrado no sistema.", response.getBody().getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void testeProdutoInsuficienteException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ProdutoInsuficienteException exception = new ProdutoInsuficienteException("PRODUTO 1");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");
        ResponseEntity<CatalogoProdutosServiceApplicationError> response = handler.validationProdutoInsuficienteException(exception, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Não há unidades suficientes do produto PRODUTO 1 para a demanda solicitada", response.getBody().getMessage());
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

        ResponseEntity<CatalogoProdutosServiceApplicationError> response = handler.validationCatalogoProdutosServiceApplicationException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de Validação", response.getBody().getError());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void testInvalidBusinessRules() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        CatalogoProdutosServiceApplicationException exception = new CatalogoProdutosServiceApplicationException("Erro de regra de negócio");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/api/resource");

        ResponseEntity<CatalogoProdutosServiceApplicationError> response = handler.validationCatalogoProdutosServiceApplicationException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void testDataIntegrityViolationException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Mensagem de erro");
        WebRequest request = mock(WebRequest.class);

        when(request.getDescription(false)).thenReturn("/api/resource");

        ResponseEntity<CatalogoProdutosServiceApplicationError> response = handler.validationCatalogoProdutosServiceApplicationException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Já existe um registro com os mesmos dados. Por favor, verifique os dados e tente novamente.", response.getBody().getError());
        assertEquals("/api/resource", response.getBody().getPath());
    }
}