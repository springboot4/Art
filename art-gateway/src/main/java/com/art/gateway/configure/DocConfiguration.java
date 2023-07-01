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

package com.art.gateway.configure;

import cn.hutool.core.collection.CollUtil;
import com.art.gateway.handler.GatewayIndexHandler;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/22 23:16
 */
@Configuration(proxyBeanMethods = false)
public class DocConfiguration {

	public static final String API_URI = "/%s/v3/api-docs";

	@Bean
	@ConditionalOnProperty(prefix = "fxz.common.doc", value = "enabled", havingValue = "true", matchIfMissing = true)
	public List<GroupedOpenApi> apis(SwaggerUiConfigProperties swaggerUiConfigProperties,
			RouteDefinitionLocator locator) {
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		if (CollUtil.isEmpty(definitions)) {
			return new ArrayList<>();
		}

		Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = definitions.stream().map(routeDefinition -> {
			AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
			String id = routeDefinition.getId();
			swaggerUrl.setName(id);
			swaggerUrl.setUrl(String.format(API_URI,
					routeDefinition.getId().substring(routeDefinition.getId().lastIndexOf("-") + 1)));
			return swaggerUrl;
		}).collect(Collectors.toSet());
		swaggerUiConfigProperties.setUrls(urls);

		return new ArrayList<>();
	}

	@Bean
	public RouterFunction<ServerResponse> docIndexHandler(GatewayIndexHandler gatewayIndexHandler) {
		return RouterFunctions.route(RequestPredicates.GET("/").and(RequestPredicates.accept(MediaType.ALL)),
				gatewayIndexHandler);
	}

}
