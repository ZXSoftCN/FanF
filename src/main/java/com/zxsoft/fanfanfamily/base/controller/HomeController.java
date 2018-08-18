package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.domain.vo.UserInfoDto;
import com.zxsoft.fanfanfamily.base.service.UserInfoService;
import com.zxsoft.fanfanfamily.base.service.impl.UserInfoServiceImpl;
import com.zxsoft.fanfanfamily.base.sys.DecryptRequestBody;
import com.zxsoft.fanfanfamily.base.sys.EncryptResponseBody;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import com.zxsoft.fanfanfamily.common.AESUtil;
import com.zxsoft.fanfanfamily.common.JWTUtil;
import com.zxsoft.fanfanfamily.config.JWTToken;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseBodyBuilder;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseBuilder;
import com.zxsoft.fanfanfamily.config.converter.FanfAppData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

@Controller
public class HomeController {


    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping({"/","/index"})
    public String index(){
        return"/index";
    }

    @RequestMapping("/loginGet")
    @ResponseBody
    public String login(HttpServletRequest request,@RequestParam(name = "username") String userName,
                        @RequestParam(name = "password") String password) throws Exception {
        System.out.println("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.


        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        Subject currentUser = SecurityUtils.getSubject();
        try {
            if (currentUser.isAuthenticated()) {
                UserInfo currUserInfo = (UserInfo) currentUser.getPrincipals().getPrimaryPrincipal();
                //不管当前用户是不是更换用户名，都登出再重新登入。
                if (!currUserInfo.getUserName().equalsIgnoreCase(token.getUsername())) {
                    currentUser.logout();
                }
            }
            currentUser.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            return e.getMessage();
        } catch (AuthenticationException e) {
            return e.getMessage();
        }

        //region Session Check Content
        UserInfo currUserInfo = (UserInfo)currentUser.getPrincipals().getPrimaryPrincipal();
        request.setAttribute("token","dfasdfa");
        String jSessionId = "";
        if (request.getRequestedSessionId() != null && !request.getRequestedSessionId().isEmpty()) {
            jSessionId = request.getRequestedSessionId();
            request.setAttribute("JSessionId", jSessionId);
        } else {
            jSessionId = request.getSession().getId();
            request.setAttribute("JSessionId",jSessionId);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token","dfasdfa");
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        responseHeaders.set("JSessionId",jSessionId);

        ShiroHttpSession shiroSession = ((ShiroHttpSession)request.getSession());
        ResponseEntity responseEntity = new ResponseEntity(null,responseHeaders, HttpStatus.OK);
        //endregion

        return "/index";
    }

    //    @Log(value = "登录跟踪")
    @PostMapping("/login")
    @ResponseBody
    public FanfAppData login(HttpServletRequest request, @RequestBody UserInfoDto userData, Map<String, Object> map) throws Exception{
        System.out.println("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.

        UsernamePasswordToken token = new UsernamePasswordToken(userData.getUserName(), userData.getPassword());
        Subject currentUser = SecurityUtils.getSubject();
        try {
            if (currentUser.isAuthenticated()) {
                if (currentUser.getPrincipals().getPrimaryPrincipal().getClass() == String.class) {
                    //则shiro已经过了JWT认证
                    String strToken = currentUser.getPrincipals().getPrimaryPrincipal().toString();
                    String userName = JWTUtil.getUsername(strToken);
                    if (!userName.equalsIgnoreCase(token.getUsername())) {
                        currentUser.logout();
                        currentUser.login(token);
                    }
                } else {
                    UserInfo currUserInfo = (UserInfo)currentUser.getPrincipals().getPrimaryPrincipal();
                    //如果当前用户更换用户名，都登出再重新登入。
                    if(!currUserInfo.getUserName().equalsIgnoreCase(token.getUsername())){
                        currentUser.logout();
                        currentUser.login(token);
                    }
                }

            } else {
                currentUser.login(token);
            }

        } catch (UnknownAccountException e) {
            throw new Exception("UnknownAccountException -- > 账号不存在。",e.getCause());
        } catch (IncorrectCredentialsException e) {
            throw new Exception("IncorrectCredentialsException -- > 密码不正确。",e.getCause());
        } catch (LockedAccountException e) {
            throw new Exception("UnknownAccountException -- > 账号已被锁定。",e.getCause());
        } catch (AuthenticationException e) {
            throw new Exception("UnknownAccountException -- > 账号认证错误。",e.getCause());
        }
        Session session = currentUser.getSession();
        UserInfo currUserInfo = null;
        String strToken="";
        if (currentUser.getPrincipals().getPrimaryPrincipal().getClass() == String.class) {
            //则shiro已经过了JWT认证
            String strOrgToken = currentUser.getPrincipals().getPrimaryPrincipal().toString();
            currUserInfo = userInfoService.findByUsername(token.getUsername()).get();
            strToken = JWTUtil.sign(token.getUsername(),currUserInfo.getPassword());

        } else {
            currUserInfo = (UserInfo)currentUser.getPrincipals().getPrimaryPrincipal();
            strToken = JWTUtil.sign(token.getUsername(),currUserInfo.getPassword());
        }
        userData.setToken(strToken);
        session.setAttribute("token",strToken);
        session.setAttribute("userName",token.getUsername());
        return FanFResponseBodyBuilder.ok("登录成功",userData);
//        return ResponseEntity.ok(currUserInfo);
//        return FanFResponseBuilder.ok(strToken,"登录成功",currUserInfo);
    }

    //    @Log(value = "登录跟踪")
    @RequestMapping("/loginByToken")
    @ResponseBody
    public FanfAppData loginByToken(@RequestParam(name = "token") String token) throws Exception{
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.

        JWTToken jwtToken = new JWTToken(token);
        Subject currentUser = SecurityUtils.getSubject();
        try {
            if (currentUser.isAuthenticated()) {
                if (currentUser.getPrincipals().getPrimaryPrincipal().getClass() == String.class) {
                    //则shiro已经过了JWT认证
                    String strToken = currentUser.getPrincipals().getPrimaryPrincipal().toString();
                    String userName = JWTUtil.getUsername(strToken);
                    if (!userName.equalsIgnoreCase(userName)) {
                        currentUser.logout();
                        currentUser.login(jwtToken);
                    }
                } else {
                    UserInfo currUserInfo = (UserInfo)currentUser.getPrincipals().getPrimaryPrincipal();
                    String userName = JWTUtil.getUsername(token);
                    //如果当前用户更换用户名，都登出再重新登入。
                    if(!currUserInfo.getUserName().equalsIgnoreCase(userName)){
                        currentUser.logout();
                        currentUser.login(jwtToken);
                    }
                }

            } else {
                currentUser.login(jwtToken);
            }

        } catch (UnknownAccountException e) {
            throw new Exception("UnknownAccountException -- > 账号不存在。",e.getCause());
        } catch (IncorrectCredentialsException e) {
            throw new Exception("IncorrectCredentialsException -- > 密码不正确。",e.getCause());
        } catch (LockedAccountException e) {
            throw new Exception("UnknownAccountException -- > 账号已被锁定。",e.getCause());
        } catch (AuthenticationException e) {
            throw new Exception("UnknownAccountException -- > 账号认证错误。",e.getCause());
        }
        Session session = currentUser.getSession();
        UserInfo currUserInfo = null;
        String strToken="";
        if (currentUser.getPrincipals().getPrimaryPrincipal().getClass() == String.class) {
            //则shiro已经过了JWT认证
            String strOrgToken = currentUser.getPrincipals().getPrimaryPrincipal().toString();
            currUserInfo = userInfoService.findByUsername(token.getUsername()).get();
            strToken = JWTUtil.sign(token.getUsername(),currUserInfo.getPassword());

        } else {
            currUserInfo = (UserInfo)currentUser.getPrincipals().getPrimaryPrincipal();
            strToken = JWTUtil.sign(token.getUsername(),currUserInfo.getPassword());
        }
        userData.setToken(strToken);
        session.setAttribute("token",strToken);
        session.setAttribute("userName",token.getUsername());
        return FanFResponseBodyBuilder.ok("登录成功",userData);
    }

    @RequestMapping("/encrypt/{value}")
    @EncryptResponseBody
    @FanfAppBody
    public String responseEncrypt(@PathVariable(required = false,name = "value") String enValue,
                                  @RequestBody String bodyString) throws Exception{

        ByteSource bsEncrypt = ByteSource.Util.bytes(enValue);;
        byte[] encodes = AESUtil.toEncrypt(enValue.getBytes("UTF-8"));//加密成字节数组
        String enString = ByteSource.Util.bytes(encodes).toBase64();//将加密的字节数组Base64编码成字符串保存

        return enString;
    }

    @RequestMapping("/decrypt/decrypt")
    @ResponseBody
    public String requestDecrypt(@RequestParam(required = false,name = "enData") String enData,
                                 @RequestBody(required = false) String enJsonData) throws UnsupportedEncodingException {

        byte[] encodes = AESUtil.toEncrypt("leolee");//加密成字节数组
        String enString = Base64.getEncoder().encodeToString(encodes);//将加密的字节数组Base64编码成字符串保存

        if (enString.equalsIgnoreCase(enData)){
            System.out.print("Equal");
        }
        byte[] deBase64Codes = Base64.getDecoder().decode(enData);
        byte[] decodes = AESUtil.toDecrypt(deBase64Codes);//解密前先进行Base64解码
        String str2 = new String(decodes, "UTF-8");//通过Base64解码成原文
        return str2;
    }

    @RequestMapping("/decrypt/decrypt2")
    @ResponseBody
    @DecryptRequestBody
    public String requestDecrypt2(
            @RequestParam(required = false,name = "enData") String enData,
            @RequestBody(required = false) String enJsonData) {
//        String jsData = enJsonData;
        return enJsonData;
    }

    @RequestMapping("/home/encryptData")
    @EncryptResponseBody
    public String queryByEncryptData(@RequestParam(required = false,name = "enData") String enData){
        if(enData.trim().isEmpty()){
            return null;
        }
        return enData;
    }

    /*
        logout后重定向到内部logout，返回Json信息
     */
    @RequestMapping("/logout")
    public FanfAppData logout(){
        System.out.println("------登出-------");
        SecurityUtils.getSubject().logout();
        return FanFResponseBodyBuilder.ok("logout",null);
//        return "403";
    }

    @RequestMapping("/403")
    public String unAuthentication(HttpServletRequest request){
        String errorMsg = request.getAttribute("error").toString();
        System.out.println("------认证错误：Unauthorized-------");
        throw new AuthenticationException();
//        return "403";
    }

    @RequestMapping("/401")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        throw new AuthorizationException();
//        return "401";
    }
}
