/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.redis.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.art.common.redis.core.mq.client.RedisMQTemplate;
import com.art.common.redis.core.mq.interceptor.RedisMessageInterceptor;
import com.art.common.redis.core.mq.pubsub.AbstractPubSubMessageListener;
import com.art.common.redis.core.mq.stream.AbstractStreamMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/6/30 16:10
 */
@SuppressWarnings("all")
@ConditionalOnProperty(prefix = "redis.mq", name = "enabled", havingValue = "true",matchIfMissing = true)
@AutoConfiguration
@Slf4j
public class RedisMqAutoConfig {

	/**
	 * Redis消息队列操作类
	 */
	@Bean
	public RedisMQTemplate redisMQTemplate(StringRedisTemplate redisTemplate,
			List<RedisMessageInterceptor> interceptors) {
		RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redisTemplate);
		interceptors.forEach(redisMQTemplate::addInterceptor);
		return redisMQTemplate;
	}

	/**
	 * StreamMessageListenerContainerOptions
	 */
	@Bean
	public StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options() {
		return StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
				// 读取超时时间
				.pollTimeout(Duration.ofSeconds(3))
				// 每次最多拉取多少条消息
				.batchSize(10)
				// 目标类型 我们手动序列化为String
				.targetType(String.class).build();
	}

	/**
	 * 创建Stream消费容器 基于消费者组 同一服务集群属于一个消费者组
	 */
	@Bean(initMethod = "start", destroyMethod = "stop")
	public StreamMessageListenerContainer<String, ObjectRecord<String, String>> redisStreamMessageListenerContainer(
			RedisMQTemplate redisMQTemplate, List<AbstractStreamMessageListener<?>> listeners,
			StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> containerOptions) {
		RedisTemplate<String, ?> redisTemplate = redisMQTemplate.getRedisTemplate();
		String consumerName = buildConsumerName();

		// 检查redis版本
		checkRedisVersion(redisTemplate);

		// 创建 StreamMessageListenerContainer 容器
		StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer
				.create(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory(), containerOptions);

		// 注册监听器 消费对应的 Stream 主题
		listeners.parallelStream().forEach(listener -> {
			// 创建 listener 对应的消费者分组
			try {
				redisTemplate.opsForStream().createGroup(listener.getStreamKey(), listener.getGroup());
			}
			catch (Exception e) {
				log.info("消费者组已存在 不在重复创建");
			}

			// 设置 listener 对应的 redisTemplate
			listener.setRedisMQTemplate(redisMQTemplate);
			// 设置 Consumer 监听
			StreamMessageListenerContainer.StreamReadRequestBuilder<String> builder = StreamMessageListenerContainer.StreamReadRequest
					// 消费偏移量 这里从消费者组最后一个没有消费的消息读取
					.builder(StreamOffset.create(listener.getStreamKey(), ReadOffset.lastConsumed()))
					// 消费者组、消费者名称信息
					.consumer(Consumer.from(listener.getGroup(), consumerName))
					// 手动ack
					.autoAcknowledge(false)
					// 默认发生异常就取消消费 我们设置为 false
					.cancelOnError(throwable -> false);
			container.register(builder.build(), listener);
		});

		return container;
	}

	/**
	 * 创建发布订阅消费容器
	 */
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisMQTemplate redisMQTemplate,
			List<AbstractPubSubMessageListener<?>> listeners) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory());
		listeners.forEach(listener -> {
			listener.setRedisMQTemplate(redisMQTemplate);
			container.addMessageListener(listener, new ChannelTopic(listener.getTopic()));
		});
		return container;
	}

	/**
	 * 构建消费者名字 使用本地 IP + 进程编号的方式
	 * @return 消费者名字
	 */
	private static String buildConsumerName() {
		return String.format("%s@%d", SystemUtil.getHostInfo().getAddress(), SystemUtil.getCurrentPID());
	}

	/**
	 * 校验Redis版本号
	 */
	private static void checkRedisVersion(RedisTemplate<String, ?> redisTemplate) {
		Properties info = redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
		String version = MapUtil.getStr(info, "redis_version");
		int majorVersion = Integer.parseInt(StrUtil.subBefore(version, '.', false));
		if (majorVersion < 5) {
			throw new IllegalStateException(
					StrUtil.format("您当前的 Redis 版本为 {}，小于最低要求的 5.0.0 版本！" + "请重新进行安装。", version));
		}
	}

}
