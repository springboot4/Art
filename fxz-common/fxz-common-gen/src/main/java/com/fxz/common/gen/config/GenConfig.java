package com.fxz.common.gen.config;

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
	 * 生成包路径
	 */
	public static String packageName;

	/**
	 * 自动去除表前缀，默认是false
	 */
	public static boolean autoRemovePre;

	public static String module;

	/**
	 * 表前缀(类名不会包含表前缀)
	 */
	public static String tablePrefix;

	@Value("${author}")
	public void setAuthor(String author) {
		GenConfig.author = author;
	}

	@Value("${packageName}")
	public void setPackageName(String packageName) {
		GenConfig.packageName = packageName;
	}

	@Value("${autoRemovePre}")
	public void setAutoRemovePre(boolean autoRemovePre) {
		GenConfig.autoRemovePre = autoRemovePre;
	}

	@Value("${tablePrefix}")
	public void setTablePrefix(String tablePrefix) {
		GenConfig.tablePrefix = tablePrefix;
	}

	@Value("${module}")
	public void setModule(String module) {
		GenConfig.module = module;
	}

}
