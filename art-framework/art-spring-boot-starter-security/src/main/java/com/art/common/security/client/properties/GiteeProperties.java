package com.art.common.security.client.properties;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.List;

/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 码云属性配置
 *
 * @author fxz
 * @date 0.0.1
 */
@Data
@ConfigurationProperties("gitee")
public class GiteeProperties {

	/**
	 * 码云属性配置列表
	 */
	private List<Gitee> list;

	/**
	 * 默认码云的权限
	 */
	private String defaultRole;

	/**
	 * 默认 AppID
	 */
	@Getter(AccessLevel.NONE)
	private String defaultAppid;

	public String getDefaultAppid() {
		if (StringUtils.hasText(defaultAppid)) {
			return defaultAppid;
		}

		return CollectionUtil.isNotEmpty(list) ? list.get(0).appid : null;
	}

	/**
	 * 码云属性配置
	 *
	 * @author fxz
	 * @date 0.0.1
	 */
	@Data
	public static class Gitee {

		/**
		 * 码云应用id
		 */
		private String appid;

		/**
		 * 码云应用密钥
		 */
		private String secret;

		/**
		 * 重定向的网址前缀（程序使用时，会在后面拼接 /{@link #appid}）
		 */
		private String redirectUriPrefix;

		/**
		 * OAuth2 客户ID
		 */
		private String clientId;

		/**
		 * OAuth2 客户秘钥
		 */
		private String clientSecret;

		/**
		 * 授权范围
		 */
		private String scope;

		/**
		 * 登录成功后重定向的URL OAuth2.1 授权 Token Name
		 */
		private String parameterName = "access_token";

		/**
		 * 登录成功后重定向的url
		 */
		private String successRedirectUrl;

		/**
		 * 我们应用授权服务器地址
		 */
		private String tokenUrl;

	}

}
