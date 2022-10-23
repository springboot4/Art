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
