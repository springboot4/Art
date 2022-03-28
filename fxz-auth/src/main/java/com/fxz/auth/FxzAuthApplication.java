package com.fxz.auth;

import com.fxz.common.security.annotation.EnableFxzCloudResourceServer;
import com.fxz.system.feign.RemoteMenuService;
import com.fxz.system.feign.RemoteUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * env
 * mysql.url=127.0.0.1;redis.url=127.0.0.1;rabbitmq.url=47.94.42.44;fxz-gateway=127.0.0.1;fxz-monitor-admin=127.0.0.1;fxz-register=127.0.0.1;nacos.url=127.0.0.1
 *
 * @author fxz
 */
@EnableFxzCloudResourceServer
@EnableFeignClients(basePackageClasses = { RemoteMenuService.class, RemoteUserService.class })
@SpringBootApplication
public class FxzAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzAuthApplication.class, args);
	}

}
