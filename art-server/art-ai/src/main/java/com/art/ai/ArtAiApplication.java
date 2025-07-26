package com.art.ai;

import com.art.common.security.resources.EnableArtResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author fxz
 */
@EnableArtResourceServer
@EnableFeignClients(basePackages = { "com.art" })
@MapperScan("com.art.ai.dao.mysql")
@SpringBootApplication
public class ArtAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtAiApplication.class, args);
	}

}
