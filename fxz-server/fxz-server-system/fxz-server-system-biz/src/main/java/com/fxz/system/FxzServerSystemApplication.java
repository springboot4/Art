package com.fxz.system;

import com.fxz.common.security.annotation.EnableFxzCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * env
 * mysql.url=127.0.0.1;redis.url=127.0.0.1;fxz-gateway=127.0.0.1;fxz-monitor-admin=127.0.0.1;fxz-register=127.0.0.1;nacos.url=127.0.0.1;fxz-tx-manager=127.0.0.1
 * vm
 * -javaagent:C:\Users\fxz\Downloads\apache-skywalking-apm-6.4.0\apache-skywalking-apm-bin\agent\skywalking-agent.jar
 * -Dskywalking.agent.service_name=fxz-server
 * -Dskywalking.collector.backend_service=47.94.42.44:11800
 *
 * @author fxz
 */
@EnableFxzCloudResourceServer
@EnableFeignClients
@MapperScan("com.fxz.system.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class FxzServerSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzServerSystemApplication.class, args);
	}

}
