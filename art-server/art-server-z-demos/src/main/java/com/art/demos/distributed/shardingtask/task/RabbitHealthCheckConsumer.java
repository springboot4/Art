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

package com.art.demos.distributed.shardingtask.task;

import com.art.demos.distributed.shardingtask.core.RedissonDispatch;
import com.art.demos.distributed.shardingtask.sharding.ShardingUtil;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 健康检查消息消费者
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/24 14:45
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(bindings = @QueueBinding(value = @Queue(),
		exchange = @Exchange(value = "exchange1", type = ExchangeTypes.FANOUT)), ackMode = "MANUAL")
public class RabbitHealthCheckConsumer {

	private final StringRedisTemplate redisTemplate;

	@SneakyThrows
	@RabbitHandler(isDefault = true)
	public void process(Message message, Channel channel) {
		// key续期
		String key = "redisson:no:" + RedissonDispatch.NO;
		if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
			redisTemplate.expire(key, 8, TimeUnit.SECONDS);
		}
		else {
			redisTemplate.opsForValue().set(key, RedissonDispatch.NO, 8, TimeUnit.SECONDS);
		}

		// ack消息
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		log.info("接收到消息：" + message);

		ShardingUtil.RedisSharding sharding = ShardingUtil.getSharding();
		log.info("分片总数:{} 机器所处分片:{}", sharding.getTotal(), sharding.getIndex());
	}

}