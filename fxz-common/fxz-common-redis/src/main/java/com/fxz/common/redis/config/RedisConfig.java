package com.fxz.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fxz.common.redis.service.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-25 13:48 默认的@Cacheable注解不支持配置过期时间，默认永不过期
 * 通过{@link CachingProperties}可以在这里的redisCacheConfigurationMap中添加key的过期配置
 */
@AllArgsConstructor
@EnableConfigurationProperties(CachingProperties.class)
@EnableCaching
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig extends CachingConfigurerSupport {

	private final CachingProperties cachingProperties;

	/**
	 * 不配置key的情况,将方法名作为缓存key名称
	 */
	@Override
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> method.getName();
	}

	/**
	 * 缓存管理器
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		return new RedisCacheManager(
				// Redis 缓存写入器
				RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
				// 默认过期策略，未配置的 key 会使用这个
				this.getRedisCacheConfigurationWithTtl(cachingProperties.getDefaultTtl()),
				// 指定 key 策略
				this.getRedisCacheConfigurationMap());
	}

	private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
		// 进行过期时间配置 key , ttl
		Set<String> keySet = cachingProperties.getKeysTtl().keySet();
		if (!CollectionUtils.isEmpty(keySet)) {
			keySet.forEach(key -> redisCacheConfigurationMap.put(key,
					this.getRedisCacheConfigurationWithTtl(cachingProperties.getKeysTtl().get(key))));
		}
		return redisCacheConfigurationMap;
	}

	/**
	 * 缓存管理器策略过期时间配置
	 */
	private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
		// 序列化方式
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.registerModule(new JavaTimeModule());
		// ObjectMapper.DefaultTyping.NON_FINAL指定序列化输入的类型
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.WRAPPER_ARRAY);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
		redisCacheConfiguration = redisCacheConfiguration
				// 设置key为String
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
				// 设置value 为自动转Json的Object
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
				// 设置过期时间
				.entryTtl(Duration.ofSeconds(seconds))
				// 覆盖默认的构造key，否则会多出一个冒号
				.computePrefixWith(name -> name + ":");

		return redisCacheConfiguration;
	}

	/**
	 * 创建 RedisTemplate Bean，使用 JSON 序列化方式
	 */
	@Bean
	@ConditionalOnClass(RedisOperations.class)
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		// 创建 RedisTemplate 对象
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		// 设置 RedisConnection 工厂。
		template.setConnectionFactory(factory);

		// 使用 String 序列化方式，序列化 KEY 。
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());

		// 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
		template.setValueSerializer(RedisSerializer.json());
		template.setHashValueSerializer(RedisSerializer.json());

		return template;
	}

	@Bean
	@ConditionalOnBean(name = "redisTemplate")
	public RedisService redisService() {
		return new RedisService();
	}

}
