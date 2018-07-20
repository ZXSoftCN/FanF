package com.zxsoft.fanfanfamily;

import com.zxsoft.fanfanfamily.config.AppPropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ServletComponentScan
public class FanfanfamilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FanfanfamilyApplication.class, args);
	}

}
