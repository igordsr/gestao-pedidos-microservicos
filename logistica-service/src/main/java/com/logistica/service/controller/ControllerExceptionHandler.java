package com.logistica.service.controller;

import com.logistica.service.infrastructure.exception.ComunicacaoApiException;
import com.logistica.service.infrastructure.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(ComunicacaoApiException.class)
    public ResponseEntity<CustomException> validationRegistroNaoEncontradoException(ComunicacaoApiException err, HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(err.getCode()).body(err);
    }


}