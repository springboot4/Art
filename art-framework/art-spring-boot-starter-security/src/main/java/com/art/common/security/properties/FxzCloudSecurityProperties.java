/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.security.properties;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.art.common.security.annotation.Ojbk;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 资源服务器属性配置
 *
 * @author fxz
 */
@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "fxz.cloud.security")
public class FxzCloudSecurityProperties implements InitializingBean {

	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

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

	/**
	 * 允许不经过网关访问的uri
	 */
	private List<String> innerUri;

	/**
	 * 访问令牌有效期(秒)
	 */
	private Integer accessTokenValiditySeconds = 60 * 60 * 24;

	/**
	 * 刷新令牌有效期(秒)
	 */
	private Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 7;

	@Override
	public void afterPropertiesSet() {
		List<String> list = new ArrayList<>();

		RequestMappingHandlerMapping mapping = SpringUtil.getBean("requestMappingHandlerMapping");
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		map.keySet().forEach(info -> {
			HandlerMethod handlerMethod = map.get(info);

			// 获取方法上边的注解 替代path variable 为 *
			Ojbk method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Ojbk.class);
			Optional.ofNullable(method)
				.ifPresent(inner -> info.getPathPatternsCondition()
					.getPatterns()
					.stream()
					.map(PathPattern::getPatternString)
					.collect(Collectors.toSet())
					.forEach(url -> list.add(ReUtil.replaceAll(url, PATTERN, "*"))));

			// 获取类上边的注解, 替代path variable 为 *
			Ojbk controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Ojbk.class);
			Optional.ofNullable(controller)
				.ifPresent(inner -> info.getPathPatternsCondition()
					.getPatterns()
					.stream()
					.map(PathPattern::getPatternString)
					.collect(Collectors.toSet())
					.forEach(url -> list.add(ReUtil.replaceAll(url, PATTERN, "*"))));

		});

		if (CollectionUtil.isNotEmpty(list)) {
			if (StringUtils.isNotEmpty(anonUris)) {
				List<String> anonUrisList = Arrays.asList(anonUris.split(StringPool.COMMA));
				list.addAll(anonUrisList);
			}
			anonUris = String.join(StringPool.COMMA, list);
		}
	}

}
