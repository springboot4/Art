package com.fxz.serversystem;

import com.common.annotation.FxzCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author fxz
 */
@MapperScan("com.fxz.serversystem.mapper")
@FxzCloudApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableDiscoveryClient
@SpringBootApplication
public class FxzServerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FxzServerSystemApplication.class, args);
    }

}
