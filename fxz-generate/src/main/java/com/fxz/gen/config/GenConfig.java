package com.fxz.gen.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-04 11:05
 */
@Getter
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = { "classpath:generator.yml" })
public class GenConfig {

	/**
	 * 作者
	 */
	public static String author;

	/**
	 * 模块名
	 */
	public static String module;

	@Value("${author}")
	public void setAuthor(String author) {
		GenConfig.author = author;
	}

	@Value("${module}")
	public void setModule(String module) {
		GenConfig.module = module;
	}

}
