package com.zxsoft.fanfanfamily.config.filter;

import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import com.zxsoft.fanfanfamily.base.sys.PageableBody;
import com.zxsoft.fanfanfamily.config.converter.BodyToFanfAppData;
import com.zxsoft.fanfanfamily.config.converter.BodyToPageableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * 将Controller返回为FanfAppData格式。
 * 提供两种响应处理：
 * 1、注解@FanfAppBody，返回ResponseEntity对象，通过ApiResponseAdvice会将其转化成FanfAppData格式；
 * 2、使用FanFResponseBodyBuilder中方法，可加入msg内容，描述服务执行信息。
 * Order顺序:(1)EncryptResponseAdvice ——> (2)ApiResponseAdvice
 */
@Order(2)
@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice {


    @Autowired
    private HttpSession session;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if(methodParameter.getMethod().isAnnotationPresent(FanfAppBody.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        Object noNullObj = o == null ? new Object() : o;
        Object obj;
        if (List.class.isAssignableFrom(noNullObj.getClass())) {
            List lstObj = (List) noNullObj;
            ListWrapper wrapper = new ListWrapper(lstObj);
            obj = BodyToFanfAppData.convert(wrapper);
        } else {
            obj = BodyToFanfAppData.convert(noNullObj);
        }
        return obj;
    }
}
