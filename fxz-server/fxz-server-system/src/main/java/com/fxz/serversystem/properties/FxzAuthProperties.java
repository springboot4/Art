package com.fxz.serversystem.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 21:00
 */
@Data
@SpringBootConfiguration
@PropertySource(value = { "classpath:fxz-auth.properties" })
@ConfigurationProperties(prefix = "fxz.auth")
public class FxzAuthProperties {

	private FxzClientsProperties[] clients = {};

}
