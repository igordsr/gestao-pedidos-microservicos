package com.catalogoprodutosservice.controller.exception.modal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
public abstract class CustomException extends RuntimeException {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    protected Date timestamp;

    @JsonProperty(value = "code")
    protected int code;

    @JsonProperty(value = "message")
    protected String message;

    @JsonProperty(value = "details")
    protected List<String> details;

    @JsonProperty(value = "path")
    protected String path;

    @Override
    public String getMessage() {
        return message;
    }

}
