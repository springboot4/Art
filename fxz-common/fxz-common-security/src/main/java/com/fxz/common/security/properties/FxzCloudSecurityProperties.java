package com.fxz.common.security.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 资源服务器属性配置
 *
 * @author fxz
 */
@ToString
@Data
@ConfigurationProperties(prefix = "fxz.cloud.security")
public class FxzCloudSecurityProperties {

	/**
	 * 是否开启安全配置
	 */
	private Boolean enable = Boolean.TRUE;

	/**
	 * 配置需要认证的uri，默认为所有/**
	 */
	private String authUri = "/**";

	/**
	 * 免认证资源路径，支持通配符 多个值时使用逗号分隔
	 */
	private String anonUris;

	/**
	 * 是否只能通过网关获取资源
	 */
	private Boolean onlyFetchByGateway = Boolean.TRUE;

}
