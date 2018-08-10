package com.zxsoft.fanfanfamily.config.converter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FanFResponseBuilder {
    public static <T> ResponseEntity<T> ok(HttpHeaders headers, String token, String msg, T t) {
        ResponseEntity<T> responseEntity = new ResponseEntity(FanFResponseEntity.ok(msg,t),
                headers, HttpStatus.OK);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(HttpHeaders headers, String token, String msg, T t) {
        ResponseEntity<T> responseEntity = new ResponseEntity(FanFResponseEntity.error(msg,t),
                headers, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> timeout(HttpHeaders headers, String token, String msg, T t) {
        ResponseEntity<T> responseEntity = new ResponseEntity(FanFResponseEntity.timeout(msg,t),
                headers, HttpStatus.REQUEST_TIMEOUT);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> ok(String token, String msg, T t) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token",token);
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ok(responseHeaders,token,msg,t);
    }

    public static <T> ResponseEntity<T> error(String token, String msg, T t) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token",token);
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return error(responseHeaders,token,msg,t);
    }

    public static <T> ResponseEntity<T> timeout(String token, String msg, T t) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token",token);
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return timeout(responseHeaders,token,msg,t);
    }
}
