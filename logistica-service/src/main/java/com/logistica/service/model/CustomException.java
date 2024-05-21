package com.logistica.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

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

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
