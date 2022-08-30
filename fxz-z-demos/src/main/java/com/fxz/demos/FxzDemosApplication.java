package com.fxz.demos;

import com.fxz.common.security.annotation.EnableFxzCloudResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/8/30 22:12
 */
@EnableFxzCloudResourceServer
@EnableFeignClients(basePackages = { "com.fxz" })
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FxzDemosApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzDemosApplication.class, args);
	}

}
