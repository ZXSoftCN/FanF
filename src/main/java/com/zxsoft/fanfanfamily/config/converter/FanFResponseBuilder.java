package com.zxsoft.fanfanfamily.config.converter;

import com.zxsoft.fanfanfamily.common.HttpContextUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于RestExceptionAdvice。
 * 标准的Rest服务不建议使用，因为Header中的token，可在ResponseHeaderHandler通过Session加入。
 * 无须手工加入。
 */
public class FanFResponseBuilder {
    private static <T> ResponseEntity<T> ok(HttpHeaders headers,  String msg, T t) {
        ResponseEntity<T> responseEntity = new ResponseEntity(FanFResponseEntity.ok(msg,t),
                headers, HttpStatus.OK);
        return responseEntity;
    }

    private static <T> ResponseEntity<T> failure(HttpHeaders headers,  String msg, T t) {
        ResponseEntity<T> responseEntity = new ResponseEntity(FanFResponseEntity.error(msg,t),
                headers, HttpStatus.OK);
        return responseEntity;
    }

    private static <T> ResponseEntity<T> error(HttpHeaders headers,  String msg, T t) {
        ResponseEntity<T> responseEntity = new ResponseEntity(FanFResponseEntity.error(msg,t),
                headers, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }

    private static <T> ResponseEntity<T> timeout(HttpHeaders headers, String msg, T t) {
        ResponseEntity<T> responseEntity = new ResponseEntity(FanFResponseEntity.timeout(msg,t),
                headers, HttpStatus.REQUEST_TIMEOUT);
        return responseEntity;
    }

    private static String getToken() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String token = request.getHeader("token");
        return token;
    }
    public static <T> ResponseEntity<T> ok(String msg, T t) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token",getToken() == null ? "" : getToken());
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ok(responseHeaders,msg,t);
    }

    public static <T> ResponseEntity<T> error(String msg, T t) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token",getToken() == null ? "" : getToken());
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return error(responseHeaders,msg,t);
    }

    public static <T> ResponseEntity<T> failure(String msg, T t) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token",getToken() == null ? "" : getToken());
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return failure(responseHeaders,msg,t);
    }

    public static <T> ResponseEntity<T> timeout(String msg, T t) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token",getToken() == null ? "" : getToken());
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return timeout(responseHeaders,msg,t);
    }
}
