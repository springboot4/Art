package com.fxz.serversystem;

import com.common.annotation.FxzCloudApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author fxz
 */
@FxzCloudApplication
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableDiscoveryClient
@SpringBootApplication
public class FxzServerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FxzServerSystemApplication.class, args);
    }

}
