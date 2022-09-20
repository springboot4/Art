package com.fxz.common.captcha.config;

import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.fxz.common.captcha.properties.AjCaptchaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 存储策略自动配置.
 * @author fxz
 */
@Configuration
public class AjCaptchaStorageAutoConfiguration {

	@Bean(name = "AjCaptchaCacheService")
	public CaptchaCacheService captchaCacheService(AjCaptchaProperties ajCaptchaProperties) {
		return CaptchaServiceFactory.getCache(ajCaptchaProperties.getCacheType().name());
	}

}
