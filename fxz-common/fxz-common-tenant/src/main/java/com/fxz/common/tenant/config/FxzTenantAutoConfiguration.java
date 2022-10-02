package com.fxz.common.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.fxz.common.mp.utils.MyBatisUtils;
import com.fxz.common.mq.redis.core.RedisMQTemplate;
import com.fxz.common.redis.cache.properties.CacheRedisCaffeineProperties;
import com.fxz.common.redis.cache.support.RedisCaffeineCacheManager;
import com.fxz.common.tenant.aspect.IgnoreTenantAspect;
import com.fxz.common.tenant.cache.TenantRedisCacheManager;
import com.fxz.common.tenant.context.FeignTenantInterceptor;
import com.fxz.common.tenant.context.TenantContextWebFilter;
import com.fxz.common.tenant.mp.TenantDatabaseHandler;
import com.fxz.common.tenant.security.TenantSecurityWebFilter;
import com.fxz.common.tenant.service.TenantValidService;
import com.fxz.system.feign.RemoteTenantService;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 多租户自动配置类
 *
 * @author Fxz
 * @version 1.0
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
	@Bean
	@Primary
	public RedisCaffeineCacheManager tenantRedisCacheManager(CacheRedisCaffeineProperties cacheRedisCaffeineProperties,
			RedisTemplate redisTemplate, RedisMQTemplate redisMQTemplate) {
		return new TenantRedisCacheManager(cacheRedisCaffeineProperties, redisTemplate, redisMQTemplate);
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
	public TenantValidService tenantValidService(RemoteTenantService remoteTenantService) {
		return new TenantValidService(remoteTenantService);
	}

}
