package com.zxsoft.fanfanfamily.common;

import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;

@Component
public class AESUtil {

    // 要求16、24、32位长度字符串
    final static String AesKeyString = "ABCDEF1234567890";//can support key sizes of 128, 192 and 256 bits*

    static byte[] aesKey ;
    static AesCipherService aesCipherService;
    static Key k;
    static {
        aesCipherService = new AesCipherService();
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//        SecretKeySpec secr = new SecretKeySpec(AesKeyString.getBytes("UTF-8"),"AES");

//        k = aesCipherService.generateNewKey();
//        aesKey = k.getEncoded();
        aesKey = ByteSource.Util.bytes(AesKeyString).getBytes();
//        aesCipherService.setKeySize(aesKey.length);
    }

    public static byte[] toEncrypt(@NotNull byte[] source){
        return aesCipherService.encrypt(source,aesKey).getBytes();
    }

    public static byte[] toDecrypt(@NotNull byte[] source){
        return aesCipherService.decrypt(source,aesKey).getBytes();
    }

    public static byte[] toEncrypt(@NotNull ByteSource source){
        return toEncrypt(source.getBytes());
    }

    public static byte[] toDecrypt(@NotNull ByteSource source){
        return toDecrypt(source.getBytes());
    }

    public static byte[] toEncrypt(@NotNull String source) throws UnsupportedEncodingException {
        return toEncrypt(source.getBytes("UTF-8"));
    }

    public static byte[] toDecrypt(@NotNull String source){
        return toDecrypt(Base64.getDecoder().decode(source));
    }
}
