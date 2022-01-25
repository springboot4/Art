package com.fxz.serversystem;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.fxz.common.security.annotation.FxzCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * env
 * mysql.url=127.0.0.1;redis.url=127.0.0.1;rabbitmq.url=47.94.42.44;fxz-gateway=127.0.0.1;fxz-monitor-admin=127.0.0.1;fxz-register=127.0.0.1;nacos.url=127.0.0.1;fxz-tx-manager=127.0.0.1
 *
 * @author fxz
 */
@MapperScan("com.fxz.serversystem.mapper")
@EnableDistributedTransaction
@FxzCloudApplication
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableDiscoveryClient
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class FxzServerTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzServerTestApplication.class, args);
	}

}
