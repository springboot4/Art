package com.fxz.common.captcha.config;

import com.fxz.common.captcha.properties.AjCaptchaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fxz
 */
@Configuration
@EnableConfigurationProperties(AjCaptchaProperties.class)
@ComponentScan("com.fxz.common.captcha")
@Import({ AjCaptchaServiceAutoConfiguration.class, AjCaptchaStorageAutoConfiguration.class })
public class AjCaptchaAutoConfiguration {

}
