package com.fxz.auth.properties;

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

	private int accessTokenValiditySeconds = 60 * 60 * 24;

	private int refreshTokenValiditySeconds = 60 * 60 * 24 * 7;

	/**
	 * 免认证路径
	 */
	private String anonUrl;

	/**
	 * 验证码配置类
	 */
	private FxzValidateCodeProperties code = new FxzValidateCodeProperties();

}
