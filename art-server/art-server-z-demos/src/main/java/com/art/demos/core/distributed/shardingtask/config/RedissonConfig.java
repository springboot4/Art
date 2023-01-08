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

package com.art.demos.core.distributed.shardingtask.config;

import org.redisson.api.ExecutorOptions;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * redisson线程池配置
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/24 11:04
 */
@Configuration
public class RedissonConfig {

	@Bean(destroyMethod = "")
	public RScheduledExecutorService rScheduledExecutorService(RedissonClient redissonClient,
			ConfigurableListableBeanFactory beanFactory) {
		// 指定重新尝试执行任务的时间间隔 设定为0则不进行重试
		ExecutorOptions executorOptions = ExecutorOptions.defaults().taskRetryInterval(0, TimeUnit.SECONDS);

		// 获取执行器
		RScheduledExecutorService scheduledExecutorService = redissonClient.getExecutorService("fxz", executorOptions);

		// 注册为worker节点
		WorkerOptions workerOptions = WorkerOptions.defaults().beanFactory(beanFactory);
		scheduledExecutorService.registerWorkers(workerOptions);

		// 如果被关了重新创建调度器
		if (scheduledExecutorService.isShutdown()) {
			scheduledExecutorService.delete();
			scheduledExecutorService = redissonClient.getExecutorService("fxz", executorOptions);
			scheduledExecutorService.registerWorkers(workerOptions);
		}

		return scheduledExecutorService;
	}

}
