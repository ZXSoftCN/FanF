package com.zxsoft.fanfanfamily.config.filter;

import com.zxsoft.fanfanfamily.base.sys.PageableBody;
import com.zxsoft.fanfanfamily.config.converter.BodyToPageableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpSession;

/*
    由ApiResponseAdvice代替支持
 */

@Deprecated
public class PageResponseAdvice implements ResponseBodyAdvice {


    @Autowired
    private HttpSession session;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
//        if(methodParameter.getMethod().isAnnotationPresent(PageableBody.class)) {
//            return true;
//        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (!(o instanceof PageImpl)) {
            return o;
        }
        String token = "";
        if (session.getAttribute("token") != null) {
            token = session.getAttribute("token").toString();
        } else {
            token = "from Request Header's token";
        }

        return BodyToPageableData.convert(o);
    }
}
