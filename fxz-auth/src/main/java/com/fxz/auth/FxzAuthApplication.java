package com.fxz.auth;

import com.fxz.common.core.annotation.EnableLettuceRedis;
import com.fxz.common.security.annotation.EnableFxzAuthExceptionHandler;
import com.fxz.common.security.annotation.EnableFxzServerProtect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * env
 * mysql.url=127.0.0.1;redis.url=127.0.0.1;rabbitmq.url=47.94.42.44;fxz-gateway=127.0.0.1;fxz-monitor-admin=127.0.0.1;fxz-register=127.0.0.1;nacos.url=127.0.0.1
 *
 * @author fxz
 */
@EnableLettuceRedis
@MapperScan("com.fxz.auth.mapper")
@EnableFxzServerProtect
@EnableFxzAuthExceptionHandler
@SpringBootApplication
public class FxzAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzAuthApplication.class, args);
	}

}