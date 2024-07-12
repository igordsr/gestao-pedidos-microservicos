package com.catalogoprodutosservice.configuration;

import com.catalogoprodutosservice.controller.exception.modal.ComunicacaoApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (Objects.equals(response.status(), HttpStatus.FORBIDDEN.value()) || Objects.equals(response.status(), HttpStatus.UNAUTHORIZED.value())) {
            return new ComunicacaoApiException(response.status(), "Falha na autenticação: o token fornecido não possui privilégios ou é inválido ou expirado.", methodKey);
        }
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
