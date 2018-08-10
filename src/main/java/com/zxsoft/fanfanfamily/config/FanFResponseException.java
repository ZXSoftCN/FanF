package com.zxsoft.fanfanfamily.config;

import org.springframework.http.HttpStatus;

public class FanFResponseException {
    private HttpStatus status;
    private String message;
    private int statusCode;

    public HttpStatus getStatus() {
        return status;
    }

    public FanFResponseException() {
    }

    public FanFResponseException(HttpStatus status, int statusCode, String message) {
        this.status = status;
        this.message = message;
        this.statusCode = statusCode;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
