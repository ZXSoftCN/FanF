package com.zxsoft.fanfanfamily.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Clock;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {


    private static final long EXPIRE_INITTIME =  1000*60*60*24*15;//60*24*7;//
    // 过期时间7天
    private static final long EXPIRE_ADDTIME =  1000*60*60*24*7;//60*24*7;//

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * 认证失败抛出异常。
     */
    public static void verify(String token, String username, String secret) {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            Clock clock = new Clock() {
                @Override
                public Date getToday() {
                    return new Date();
                }
            };

            JWTVerifier.BaseVerification baseVerification = (JWTVerifier.BaseVerification) JWT.require(algorithm)
                    .withClaim("username", username).withIssuer("ZXSoft").withSubject("FanfFamily");

            JWTVerifier verifier = baseVerification.build(clock);

            DecodedJWT jwt = verifier.verify(token);

    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /*
        获取JWT全部Claim值
     */
    public static Map<String,String> getClainms(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> map = jwt.getClaims();
            Map<String, String> resultMap = new HashMap<>();
            map.forEach((k,v) -> resultMap.put(k, v.asString()));
            return resultMap;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /*
        延长token有效期
     */
    public static String reSetExpiredTime(String token,String secret) {
        DecodedJWT jwt = JWT.decode(token);
        System.out.println("延长前过期时间为：" + jwt.getExpiresAt().toString());
        String oldToken = jwt.getToken();

        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date date = new Date(System.currentTimeMillis() + EXPIRE_ADDTIME);
        System.out.println(date.toString());

        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer("ZXSoft");
        builder.withSubject("FanfFamily");
        //复写原Token中的Claims
        Map<String, Claim> map = jwt.getClaims();
        map.forEach((k, v) -> {
            builder.withClaim(k,v.asString());
        });
        builder.withExpiresAt(date);

        String newToken = builder.sign(algorithm);//签名

        DecodedJWT jwtNew = JWT.decode(newToken);
        System.out.println("延长后过期时间为："+ jwtNew.getExpiresAt().toString());
        Assert.isTrue(!oldToken.equalsIgnoreCase(newToken), "Token new Time Refresh");
        return newToken;
    }

    public static DateTime getExpiredTime(String token) {
        DecodedJWT jwt = JWT.decode(token);
        System.out.println(jwt.getExpiresAt());
        Date date =  jwt.getExpiresAt();
        return DateTime.parse(date.toString());
    }

    /**
     * 生成签名,5min后过期
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        try {
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.SECOND,30 ); //30秒后
//            Date date = calendar.getTime();
            Date date = new Date(System.currentTimeMillis() + EXPIRE_INITTIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            System.out.println(date.toString());
            // 附带username信息
            return JWT.create()
                    .withIssuer("ZXSoft")
                    .withSubject("FanfFamily")
                    .withExpiresAt(date)
                    .withClaim("username", username)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }
}
