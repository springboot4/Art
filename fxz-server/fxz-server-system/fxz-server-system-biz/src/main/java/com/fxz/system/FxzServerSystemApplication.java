package com.fxz.system;

import com.common.database.annotation.EnableDynamicDataSource;
import com.fxz.common.security.annotation.EnableFxzCloudResourceServer;
import com.fxz.system.dinger.handler.DingTalkMultiHandler;
import com.github.jaemon.dinger.core.annatations.DingerScan;
import com.github.jaemon.dinger.core.entity.enums.DingerType;
import com.github.jaemon.dinger.multi.annotations.EnableMultiDinger;
import com.github.jaemon.dinger.multi.annotations.MultiDinger;
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
@EnableDynamicDataSource
@EnableMultiDinger(
// 启用钉钉多机器人配置
@MultiDinger(dinger = DingerType.DINGTALK, handler = DingTalkMultiHandler.class))
@DingerScan(basePackages = "com.fxz.system.dinger")
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
