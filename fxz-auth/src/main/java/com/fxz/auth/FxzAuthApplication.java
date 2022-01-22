package com.fxz.auth;

import com.fxz.common.security.annotation.EnableFxzAuthExceptionHandler;
import com.fxz.common.security.annotation.EnableFxzServerProtect;
import com.fxz.common.core.annotation.EnableLettuceRedis;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
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
