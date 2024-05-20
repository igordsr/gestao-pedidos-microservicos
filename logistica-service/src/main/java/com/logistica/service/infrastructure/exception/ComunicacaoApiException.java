package src.main.java.com.logistica.service.infrastructure.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ComunicacaoApiException extends CustomException {

    public ComunicacaoApiException(int code, String message, String path) {
        this.code = code;
        this.message = message;
        this.path = path;
    }
}
