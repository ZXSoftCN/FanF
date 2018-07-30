package com.zxsoft.fanfanfamily;

import com.zxsoft.fanfanfamily.mort.repository.LimitStatusDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
//@ServletComponentScan
public class FanfanfamilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FanfanfamilyApplication.class, args);
	}

}
