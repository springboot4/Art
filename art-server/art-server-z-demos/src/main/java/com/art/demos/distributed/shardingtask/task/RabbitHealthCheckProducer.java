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

import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 健康检查消息生产者
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/24 14:45
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitHealthCheckProducer implements Runnable, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void run() {
		RabbitTemplate rabbitTemplate = SpringUtil.getBean(RabbitTemplate.class);
		String message = LocalDateTime.now().toString();
		rabbitTemplate.convertAndSend("exchange1", "", message);
	}

}
