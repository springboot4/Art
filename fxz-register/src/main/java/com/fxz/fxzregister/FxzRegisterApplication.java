package com.fxz.fxzregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 开启Eureka服务
 *
 * @author fxz
 */
@EnableEurekaServer
@SpringBootApplication
public class FxzRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzRegisterApplication.class, args);
	}

}
