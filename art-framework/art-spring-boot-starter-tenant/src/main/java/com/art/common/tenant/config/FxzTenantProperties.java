/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.tenant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * 多租户属性配置
 *
 * @author Fxz
 * @version 0.0.1
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
