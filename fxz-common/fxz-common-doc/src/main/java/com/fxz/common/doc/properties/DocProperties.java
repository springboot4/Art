package com.fxz.common.doc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/21 21:05
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "fxz.common.doc")
public class DocProperties {

	/**
	 * 是否开启
	 */
	private boolean enabled = true;

	/**
	 * 标题
	 */
	private String title = "Art开发文档";

	/**
	 * 作者
	 */
	private String author = "fxz";

	/**
	 * 描述
	 */
	private String description = "描述";

	/**
	 * 版本
	 */
	private String version = "0.0.1";

	/**
	 * 扫描模块
	 */
	private Map<String, List<String>> basePackages = new LinkedHashMap<>();

}
