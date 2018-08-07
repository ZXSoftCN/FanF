package com.zxsoft.fanfanfamily.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxsoft.fanfanfamily.base.sys.EncryptResponseBody;
import com.zxsoft.fanfanfamily.common.AESUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@RestControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if(methodParameter.getMethod().isAnnotationPresent(EncryptResponseBody.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if(methodParameter.getMethod().isAnnotationPresent(EncryptResponseBody.class)){
            EncryptResponseBody annoEncrypt = methodParameter.getMethod().getAnnotation(EncryptResponseBody.class);
            //如果加密
            if(annoEncrypt.encryt() && o != null){
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String result =  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
                    byte[] enDatas = AESUtil.toEncrypt(result);//加密
                    return Base64.getEncoder().encodeToString(enDatas);//Base64编码
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
