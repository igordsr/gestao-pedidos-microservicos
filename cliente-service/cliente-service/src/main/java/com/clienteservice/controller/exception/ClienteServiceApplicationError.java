package com.clienteservice.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ClienteServiceApplicationError {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}