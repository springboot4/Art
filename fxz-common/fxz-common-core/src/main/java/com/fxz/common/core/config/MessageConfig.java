package com.fxz.common.core.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/5/13 23:10
 */
@Configuration(proxyBeanMethods = false)
public class MessageConfig {

	/**
	 * 国际化文件配置
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		return messageSource;
	}

}
