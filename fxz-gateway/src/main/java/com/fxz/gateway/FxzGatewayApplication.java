package com.fxz.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 开启Zuul服务网关功能
 * 开启服务注册与发现
 * @author fxz
 */
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class FxzGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FxzGatewayApplication.class, args);
    }

}
