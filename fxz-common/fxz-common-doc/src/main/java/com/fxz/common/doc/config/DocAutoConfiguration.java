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

package com.fxz.common.doc.config;

import com.fxz.common.doc.properties.DocProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/21 21:02
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(DocProperties.class)
@AutoConfiguration
public class DocAutoConfiguration {

	private final ServiceInstance serviceInstance;

	private final DocProperties docProperties;

	@Bean
	@ConditionalOnProperty(prefix = "fxz.common.doc", value = "enabled", havingValue = "true", matchIfMissing = true)
	public OpenAPI springShopOpenAPI() {
		// 获取服务实例信息
		List<Server> serverList = new ArrayList<>();
		String serviceId = serviceInstance.getServiceId();
		Map<String, String> services = docProperties.getServices();

		// 获取服务路由前缀
		String path = services.get(serviceId);
		serverList.add(new Server().url(docProperties.getUrl() + "/" + path));

		return new OpenAPI().servers(serverList)
				.info(new Info().title(docProperties.getTitle()).description(docProperties.getDescription())
						.version(docProperties.getVersion()).contact(new Contact().name(docProperties.getAuthor())));
	}

}
