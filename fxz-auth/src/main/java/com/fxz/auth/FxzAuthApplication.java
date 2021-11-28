package com.fxz.auth;

import com.common.annotation.EnableFxzAuthExceptionHandler;
import com.common.annotation.EnableFxzServerProtect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author fxz
 */
@EnableFxzServerProtect
@EnableFxzAuthExceptionHandler
@EnableDiscoveryClient
@SpringBootApplication
public class FxzAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FxzAuthApplication.class, args);
    }

}
