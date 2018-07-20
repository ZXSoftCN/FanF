package com.zxsoft.fanfanfamily.common;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;

public class RandomGeneratorUtil {

    private RandomGeneratorUtil(){}

    public static SecureRandomNumberGenerator getSecRng() {
        if (secRng == null) {
            synchronized (RandomGeneratorUtil.class) {
                if (secRng == null){
                    secRng = new SecureRandomNumberGenerator();//双重校验锁法
                }
            }
        }
        return secRng;
    }

    private static SecureRandomNumberGenerator secRng;


}
