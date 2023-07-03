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

package com.art.mq.sdk.support.group;

import cn.hutool.core.util.TypeUtil;
import com.art.mq.common.message.AbstractGroupMessage;
import com.art.mq.common.message.AbstractMessage;
import com.art.mq.sdk.client.HazelcastMQTemplate;
import com.art.mq.sdk.interceptor.HazelcastMessageInterceptor;
import com.art.mq.sdk.support.HazelcastMessageListener;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/24 20:17
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractHazelcastGroupMessageListener<T extends AbstractGroupMessage>
		implements HazelcastMessageListener<T> {

	/**
	 * 消息类型
	 */
	private final Class<T> messageType;

	/**
	 * topic
	 */
	private final String group;

	private HazelcastMQTemplate mqTemplate;

	@SneakyThrows
	protected AbstractHazelcastGroupMessageListener() {
		this.messageType = (Class<T>) TypeUtil.getTypeArgument(getClass(), 0);
		this.group = messageType.newInstance().getGroup();
	}

	@Override
	public void process(T message) {
		// 消费消息
		try {
			log.info("message:{}", message);
			// 消费前处理
			consumeMessageBefore(message);
			// 消费消息
			this.onMessage(message);
		}
		finally {
			// 消费后处理
			consumeMessageAfter(message);
		}
	}

	protected void consumeMessageAfter(AbstractMessage message) {
		List<HazelcastMessageInterceptor> interceptors = mqTemplate.getInterceptors();
		interceptors.forEach(interceptor -> interceptor.consumeMessageBefore(message));
	}

	protected void consumeMessageBefore(AbstractMessage message) {
		List<HazelcastMessageInterceptor> interceptors = mqTemplate.getInterceptors();
		for (int i = interceptors.size() - 1; i >= 0; i--) {
			interceptors.get(i).consumeMessageAfter(message);
		}
	}

}
