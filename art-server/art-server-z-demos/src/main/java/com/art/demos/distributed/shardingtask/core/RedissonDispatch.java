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

package com.art.demos.distributed.shardingtask.core;

import cn.hutool.core.lang.UUID;
import com.art.demos.distributed.shardingtask.task.RabbitHealthCheckProducer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScheduledExecutorService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 健康检查调度任务者
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/24 10:56
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonDispatch implements ApplicationRunner {

	private final RScheduledExecutorService rScheduledExecutorService;

	private final StringRedisTemplate redisTemplate;

	private final RabbitHealthCheckProducer rabbitHealthCheckProducer;

	public static final String NO = UUID.randomUUID().toString();

	@SneakyThrows
	@Override
	public void run(ApplicationArguments args) {
		String key = "redisson:no:" + NO;
		redisTemplate.opsForValue().set(key, NO, 8, TimeUnit.SECONDS);

		// 保证仅创建一个Master实例
		Boolean result = redisTemplate.opsForValue().setIfAbsent("redisson:send", "redisson:send", 30, TimeUnit.DAYS);
		if (Boolean.FALSE.equals(result)) {
			return;
		}

		log.info("抢锁成功！");

		// todo 记录taskId 停止调度
		rScheduledExecutorService.scheduleAtFixedRate(rabbitHealthCheckProducer, 0, 5, TimeUnit.SECONDS).getTaskId();
	}

}
