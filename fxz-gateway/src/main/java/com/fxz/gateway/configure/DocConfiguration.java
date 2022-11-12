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

package com.fxz.gateway.configure;

import com.fxz.gateway.properties.DocProperties;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/22 23:16
 */
@Configuration
public class DocConfiguration {

	public static final String API_URI = "/%s/v3/api-docs";

	@Bean
	@ConditionalOnProperty(prefix = "fxz.common.doc", value = "enabled", havingValue = "true", matchIfMissing = true)
	public List<GroupedOpenApi> apis(SwaggerUiConfigProperties swaggerUiConfigProperties, DocProperties docProperties) {
		Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
		docProperties.getServices().forEach((k, v) -> {
			AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
			swaggerUrl.setName(k);
			swaggerUrl.setUrl(String.format(API_URI, v));
			urls.add(swaggerUrl);
		});
		swaggerUiConfigProperties.setUrls(urls);

		return new ArrayList<>();
	}

}
