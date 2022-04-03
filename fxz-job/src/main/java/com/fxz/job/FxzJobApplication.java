package com.fxz.job;

import com.fxz.common.security.annotation.EnableFxzCloudResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-04-03 17:16
 */
@SpringBootApplication
@EnableFxzCloudResourceServer
public class FxzJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzJobApplication.class, args);
	}

}
