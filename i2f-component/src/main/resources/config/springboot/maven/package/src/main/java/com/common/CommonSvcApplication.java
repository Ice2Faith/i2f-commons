package com.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages="com.common")
@MapperScan("com.common.**.mapper")
@EnableFeignClients
public class CommonSvcApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommonSvcApplication.class,args);
	}
}
