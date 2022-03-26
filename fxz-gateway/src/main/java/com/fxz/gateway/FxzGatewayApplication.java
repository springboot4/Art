package com.fxz.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * env
 * mysql.url=127.0.0.1;redis.url=127.0.0.1;rabbitmq.url=47.94.42.44;fxz-gateway=127.0.0.1;fxz-monitor-admin=127.0.0.1;fxz-register=127.0.0.1;nacos.url=127.0.0.1
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-12-07 10:10
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FxzGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzGatewayApplication.class, args);
	}

}
