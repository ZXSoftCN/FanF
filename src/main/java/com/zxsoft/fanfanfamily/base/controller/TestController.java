package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.common.AESUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Controller
public class TestController {

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/testGet/{paramString}")
    public String testGet(@PathVariable String paramString) throws Exception{
        //测试AES加密
        ByteSource bsEncrypt = ByteSource.Util.bytes(paramString);
        byte[] encodes = AESUtil.toEncrypt(paramString.getBytes("UTF-8"));//加密成字节数组
        String enString = ByteSource.Util.bytes(encodes).toBase64();//将加密的字节数组Base64编码成字符串保存
        ByteSource deDecrypt = ByteSource.Util.bytes(encodes);

        byte[] undecodes = Base64.getDecoder().decode(enString);//将保存的字符串进行Base64解码成字节数组
        byte[] decodes = AESUtil.toDecrypt(undecodes);//再进行解密
//        String deString = ByteSource.Util.bytes(decodes).toBase64();//解密后字节数据转出Base64字符串，供后续Base64解码
        String str2 = new String(decodes, "UTF-8");//通过Base64解码成原文
        return str2;
    }


    @GetMapping("/testGet2/{paramString}")
    public String testGet2(@PathVariable String paramString) throws Exception{
        //测试AES加密
        ByteSource bsEncrypt = ByteSource.Util.bytes(paramString);
        byte[] encodes = AESUtil.toEncrypt(paramString.getBytes("UTF-8"));//加密成字节数组
        byte[] decodes = AESUtil.toDecrypt(encodes);
        String s = new String(decodes,"UTF-8");//直接将解密的字节数组转化成明文

        String enString = ByteSource.Util.bytes(encodes).toBase64();//将加密的字节数组Base64编码成字符串保存

        //因为加密的字节数组保存前进行了Base64编码，所以解密前先做Base64解码
        byte[] decodes2 = AESUtil.toDecrypt(Base64.getDecoder().decode(enString));//将保存的字符串进行Base64解码成字节数组,再进行解密
        String str2 = new String(decodes2, "UTF-8");//将解密后的字节数组解码成原文
        return str2;
    }

}