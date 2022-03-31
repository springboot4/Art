package com.fxz.gen;

import com.common.database.annotation.EnableDynamicDataSource;
import com.fxz.common.security.annotation.EnableFxzCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 16:28
 */
@EnableDynamicDataSource
@EnableFxzCloudResourceServer
@SpringBootApplication
@MapperScan("com.fxz.gen")
public class FxzGenerateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzGenerateApplication.class, args);
	}

}
