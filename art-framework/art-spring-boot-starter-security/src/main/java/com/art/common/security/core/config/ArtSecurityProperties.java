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

package com.art.common.security.core.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.art.common.security.core.annotation.Ojbk;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class ArtSecurityProperties implements InitializingBean {

	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

	/**
	 * 免认证资源路径，支持通配符 多个值时使用逗号分隔
	 */
	private String anonUris;

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
