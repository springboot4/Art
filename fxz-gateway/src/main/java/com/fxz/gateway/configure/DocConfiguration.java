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
 * @version 1.0
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
