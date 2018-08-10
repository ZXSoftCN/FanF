package com.zxsoft.fanfanfamily.config.filter;

import com.zxsoft.fanfanfamily.common.HttpContextUtils;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseBuilder;
import com.zxsoft.fanfanfamily.config.FanFResponseException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/*
    只应用@Controller的控制器组件，不影响@RestController组件。
    @ControllerAdvice中方法需要注解@ResponseBody对结果进行json化传出。

    替代PostHandler作为拦截器使用
 */
@ControllerAdvice(annotations = Controller.class)
public class RestExceptionAdvice {
    @Autowired
    private WebApplicationContext context;

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<FanFResponseException> handleException(AuthenticationException e){
        FanFResponseException exception = new FanFResponseException(HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.value(),
                String.format("认证失败：%s", e.getMessage()));
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String token = request.getHeader("token");

        return FanFResponseBuilder.error(token,"认证失败！",exception);
    }

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    public ResponseEntity<FanFResponseException> handleException(AuthorizationException e){

        FanFResponseException exception = new FanFResponseException(HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.value(),
                String.format("未被授权：%s", e.getMessage()));
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String token = request.getHeader("token");

        return FanFResponseBuilder.error(token,"未被授权",exception);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity handleException(Exception e){
        FanFResponseException exception = new FanFResponseException(HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                String.format("ExceptionMsg:%s",e.getMessage()));
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String token = request.getHeader("token");

        return FanFResponseBuilder.error(token,String.format("HttpStatus:%d,ExceptionMsg:%s",HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage()),exception);
    }
}
