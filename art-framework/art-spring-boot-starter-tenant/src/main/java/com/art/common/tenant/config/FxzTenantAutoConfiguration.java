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

import com.art.cache.sdk.properties.CacheRedisCaffeineProperties;
import com.art.common.tenant.aspect.IgnoreTenantAspect;
import com.art.common.tenant.cache.TenantRedisCacheManager;
import com.art.common.tenant.cache.TenantRedisCaffeineCacheManager;
import com.art.common.tenant.context.FeignTenantInterceptor;
import com.art.common.tenant.context.TenantContextWebFilter;
import com.art.common.tenant.mp.TenantDatabaseHandler;
import com.art.common.tenant.security.TenantSecurityWebFilter;
import com.art.common.tenant.service.TenantValidService;
import com.art.mq.sdk.client.RedisMQTemplate;
import com.art.mybatis.common.utils.MyBatisUtils;
import com.art.system.api.tenant.TenantServiceApi;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.Objects;

/**
 * 多租户自动配置类
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/1 11:08
 */
@SuppressWarnings("all")
@AutoConfiguration
@ConditionalOnProperty(prefix = "fxz.tenant", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(FxzTenantProperties.class)
public class FxzTenantAutoConfiguration {

	/**
	 * Mybytis-plus多租户拦截器
	 * @param properties 多租户配置
	 * @param interceptor Mybatis-plus拦截器
	 * @return Mybatis-plus多租户拦截器
	 */
	@Bean
	public TenantLineInnerInterceptor tenantLineInnerInterceptor(FxzTenantProperties properties,
			MybatisPlusInterceptor interceptor) {
		// Mybatis-plus多租户拦截器
		TenantLineInnerInterceptor inner = new TenantLineInnerInterceptor(new TenantDatabaseHandler(properties));
		// 添加拦截器到首个位置 保证在分页插件前执行
		MyBatisUtils.addInterceptor(interceptor, inner, 0);
		return inner;
	}

	/**
	 * 多租户上下文过滤器
	 */
	@Bean
	public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
		FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TenantContextWebFilter());
		registrationBean.setOrder(-110);
		return registrationBean;
	}

	/**
	 * 租户合法校验过滤器
	 */
	@Bean
	public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter(FxzTenantProperties tenantProperties,
			TenantValidService tenantValidService) {
		FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TenantSecurityWebFilter(tenantProperties, tenantValidService));
		registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER + 1);
		return registrationBean;
	}

	/**
	 * 传递Feign租户请求头
	 */
	@Bean
	public RequestInterceptor feignTenantInterceptor() {
		return new FeignTenantInterceptor();
	}

	/**
	 * 多级缓存支持多租户
	 */
	@ConditionalOnProperty(prefix = "redis.cache.multi", name = "enabled", havingValue = "true")
	@Bean
	@Primary
	public CacheManager cacheManager(CacheRedisCaffeineProperties cacheRedisCaffeineProperties,
			RedisTemplate redisTemplate, RedisMQTemplate redisMQTemplate) {
		return new TenantRedisCaffeineCacheManager(cacheRedisCaffeineProperties, redisTemplate, redisMQTemplate);
	}

	/**
	 * 多级缓存不开启时生效
	 */
	@ConditionalOnProperty(prefix = "redis.cache.multi", name = "enabled", havingValue = "false",
			matchIfMissing = false)
	@Bean
	@Primary
	public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
		RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
		RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.serializeValuesWith(
					RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
		return new TenantRedisCacheManager(cacheWriter, redisCacheConfiguration);
	}

	/**
	 * 多租户aop处理
	 */
	@Bean
	public IgnoreTenantAspect ignoreTenantAspect() {
		return new IgnoreTenantAspect();
	}

	/**
	 * 租户信息校验
	 */
	@Bean
	public TenantValidService tenantValidService(TenantServiceApi tenantServiceApi) {
		return new TenantValidService(tenantServiceApi);
	}

}
