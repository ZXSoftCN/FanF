package com.zxsoft.fanfanfamily.config;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.service.UserInfoService;
import com.zxsoft.fanfanfamily.common.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyJWTRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyJWTRealm.class);

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JWTUtil.getUsername(principals.toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

//        UserBean user = userService.getUser(username);
//        simpleAuthorizationInfo.addRole(user.getRole());
//        Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
//        simpleAuthorizationInfo.addStringPermissions(permission);

        UserInfo userInfoByQuery = userInfoService.findByUsername(username).get();
        UserInfo userInfo  = (UserInfo)principals.getPrimaryPrincipal();
        for(Role role:userInfo.getRoleList()){
            simpleAuthorizationInfo.addRole(role.getRoleName());
            for(Permission p:role.getPermissions()){
                simpleAuthorizationInfo.addStringPermission(p.getPermission());
            }
        }


        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        UserInfo userBean = userInfoService.findByUsername(username).get();
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }
        Subject currentUser = SecurityUtils.getSubject();
        Boolean isFirst = true;
        if (currentUser.isAuthenticated()) {
            isFirst = false;
        }

        try {
            JWTUtil.verify(token, username, userBean.getPassword());//verify认证会抛出异常。
        }catch (TokenExpiredException exception) {
            throw new AuthenticationException("认证过期，重新登录。");
        } catch (InvalidClaimException exception) {
            throw new AuthenticationException("用户、密码认证错误，单点登录失败。",exception.getCause());
        } catch (JWTVerificationException exception) {
            throw new AuthenticationException(exception.getMessage(),exception.getCause());
        } catch (Exception exception) {
            throw new AuthenticationException(exception.getMessage(),exception.getCause());
        }
        if (isFirst) {
            String newToken = JWTUtil.reSetExpiredTime(token,userBean.getPassword());
            Session session = currentUser.getSession();
            session.setAttribute("token",newToken);
            session.setAttribute("userName",username);

        }

        return new SimpleAuthenticationInfo(token, token, "my_jwtrealm");
    }
}
