package com.fxz.common.doc.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.fxz.common.doc.properties.DocProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/21 21:02
 */
@EnableConfigurationProperties(DocProperties.class)
@AutoConfiguration
public class DocAutoConfiguration implements ImportBeanDefinitionRegistrar, EnvironmentAware {

	private DocProperties docProperties;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		if (!docProperties.isEnabled()) {
			return;
		}

		// 扫描模块
		Map<String, List<String>> basePackages = this.docProperties.getBasePackages();
		if (MapUtil.isEmpty(basePackages)) {
			basePackages.put("空白页", CollectionUtil.newArrayList("null.null"));
		}

		LongAdder longAdder = new LongAdder();
		basePackages.forEach((group, basePackage) -> {
			longAdder.increment();
			// 扫描包
			String[] packages = ArrayUtil.toArray(basePackage, String.class);
			// 创建BeanDefinition
			BeanDefinition bean = new RootBeanDefinition(GroupedOpenApi.class, () -> this.createApi(group, packages));
			// 向容器注入bean
			registry.registerBeanDefinition(longAdder.longValue() + "APi", bean);
		});
	}

	@Override
	public void setEnvironment(@NotNull Environment environment) {
		String docPropertiesPrefix = "fxz.common.doc";
		BindResult<DocProperties> bind = Binder.get(environment).bind(docPropertiesPrefix, DocProperties.class);
		this.docProperties = bind.get();
	}

	/**
	 * 创建swagger文档
	 * @param group 模块分组
	 * @param basePackage 扫描包名
	 */
	private GroupedOpenApi createApi(String group, String[] basePackage) {
		return GroupedOpenApi.builder().group(group).packagesToScan(basePackage).build();
	}

	@Bean
	@ConditionalOnProperty(prefix = "fxz.common.doc", value = "enabled", havingValue = "true", matchIfMissing = true)
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI().info(new Info().title(docProperties.getTitle()).description(docProperties.getDescription())
				.version(docProperties.getVersion()).contact(new Contact().name(docProperties.getAuthor())));
	}

}
