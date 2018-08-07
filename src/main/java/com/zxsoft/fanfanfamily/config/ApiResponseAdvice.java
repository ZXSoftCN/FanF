package com.zxsoft.fanfanfamily.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxsoft.fanfanfamily.base.sys.EncryptResponseBody;
import com.zxsoft.fanfanfamily.base.sys.PageableBody;
import com.zxsoft.fanfanfamily.config.converter.BodyToPageableData;
import com.zxsoft.fanfanfamily.config.converter.PageableData;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if(methodParameter.getMethod().isAnnotationPresent(PageableBody.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (!(o instanceof PageImpl)) {
            return o;
        }
        return BodyToPageableData.convert(o);
    }
}
