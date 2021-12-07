package com.fxz.fxzmonitoradmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fxz
 */
@EnableAdminServer
@SpringBootApplication
public class FxzMonitorAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxzMonitorAdminApplication.class, args);
	}

}
