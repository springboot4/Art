package com.fxz.common.tenant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * 多租户属性配置
 *
 * @author Fxz
 * @version 1.0
 * @date 2022/10/1 11:09
 */
@Data
@ConfigurationProperties(prefix = "fxz.tenant")
public class FxzTenantProperties {

	/**
	 * 多租户是否开启
	 */
	private Boolean enable = true;

	/**
	 * 租户列名称
	 */
	private String column = "tenant_id";

	/**
	 * 忽略携带tenant-id的请求头
	 */
	private Set<String> ignoreUrls = Collections.emptySet();

	/**
	 * 开启多租户的表
	 * <p>
	 * 添加需要开启多租户功能的表
	 */
	private Set<String> tables = Collections.emptySet();

}
