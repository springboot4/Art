package com.fxz.fxzmonitoradmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * env
 * mysql.url=127.0.0.1;redis.url=127.0.0.1;rabbitmq.url=47.94.42.44;fxz-gateway=127.0.0.1;fxz-monitor-admin=127.0.0.1;fxz-register=127.0.0.1
 *
 * @author fxz
 */
@EnableAdminServer
@SpringBootApplication
public class FxzMonitorAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzMonitorAdminApplication.class, args);
	}

}
