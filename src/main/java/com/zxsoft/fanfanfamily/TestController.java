package com.zxsoft.fanfanfamily;

import com.zxsoft.fanfanfamily.config.AppPropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class TestController {
    @Autowired
    AppPropertiesConfig propConfig;

    @RequestMapping("/")
    public String index(){
        return "Hello world!" + propConfig.getContentKey() + propConfig.getRandomInt();
    }
}
