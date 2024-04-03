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

package com.art.common.lock.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RTopic;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.PatternMessageListener;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/28 20:06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedissonUtils {

	private static final RedissonClient REDISSON_CLIENT = SpringUtil.getBean(RedissonClient.class);

	/**
	 * 限流器
	 * @param limiterKey 限流key
	 * @param rateType 限流类型 (分布式 or jvm)
	 * @param rate 令牌生产速率
	 * @param rateInterval 时间间隔
	 * @return -1表示失败 其他表示剩余令牌数
	 */
	public static Long rateLimiter(String limiterKey, RateType rateType, int rate, int rateInterval) {
		RRateLimiter rateLimiter = REDISSON_CLIENT.getRateLimiter(limiterKey);
		rateLimiter.trySetRate(rateType, rate, rateInterval, RateIntervalUnit.SECONDS);

		if (rateLimiter.tryAcquire()) {
			return rateLimiter.availablePermits();
		}
		return -1L;
	}

	/**
	 * 订阅消息（正则匹配）
	 * @param topic topic
	 * @param clazz 消息类型
	 * @param messageListener 监听逻辑
	 */
	public static <T> void subscribePatternTopic(String topic, Class<T> clazz,
			PatternMessageListener<T> messageListener) {
		RPatternTopic rTopic = REDISSON_CLIENT.getPatternTopic(topic);
		rTopic.addListener(clazz, messageListener);
	}

	/**
	 * 订阅消息
	 * @param topic topic
	 * @param clazz 消息类型
	 * @param messageListener 监听逻辑
	 */
	public static <T> void subscribe(String topic, Class<T> clazz, MessageListener<T> messageListener) {
		RTopic rTopic = REDISSON_CLIENT.getTopic(topic);
		rTopic.addListener(clazz, messageListener);
	}

	/**
	 * 发布消息
	 * @param topic topic
	 * @param msg 消息
	 */
	public static <T> void publish(String topic, T msg) {
		RTopic rTopic = REDISSON_CLIENT.getTopic(topic);
		rTopic.publish(msg);
	}

}
