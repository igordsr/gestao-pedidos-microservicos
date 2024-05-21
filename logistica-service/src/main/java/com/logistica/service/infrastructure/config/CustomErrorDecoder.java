package com.logistica.service.infrastructure.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistica.service.infrastructure.exception.ComunicacaoApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream bodyIs = response.body().asInputStream()) {
            final ObjectMapper objectMapper = new ObjectMapper();
            switch (response.status()) {
                case 400:
                case 404:
                case 409:
                case 422:
                    return objectMapper.readValue(bodyIs, ComunicacaoApiException.class);
                default:
                    return new ComunicacaoApiException(HttpStatus.BAD_GATEWAY.value(), "Falha na comunicação com os servidores. Por favor, tente novamente mais tarde.", methodKey);
            }
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
    }
}
