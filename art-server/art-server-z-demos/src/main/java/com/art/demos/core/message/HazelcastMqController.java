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

package com.art.demos.core.message;

import com.art.common.core.model.Result;
import com.art.common.hazelcast.core.mq.HazelcastMQTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/24 21:45
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/mq")
public class HazelcastMqController {

	private final HazelcastMQTemplate hazelcastMQTemplate;

	@GetMapping("/send")
	public Result<Void> sendMsg(String msg) {
		DemoTopicMessage topicMessage = new DemoTopicMessage("广播消息,所有的客户端都可以监听到" + msg, LocalDateTime.now());
		hazelcastMQTemplate.send(topicMessage);

		DemoGroupMessage groupMessage = new DemoGroupMessage("消费者组消息,只有一个客户端都可以监听到 " + msg, LocalDateTime.now());
		hazelcastMQTemplate.send(groupMessage);
		return Result.success();
	}

}
