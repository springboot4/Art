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

package com.art.common.hazelcast.core.mq;

import cn.hutool.core.util.TypeUtil;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/24 20:09
 */
@Getter
@Setter
@SuppressWarnings("unchecked")
public abstract class AbstractTopicMessageListener<T extends AbstractTopicMessage>
		implements MessageListener<T>, HazelcastMessageListener<T> {

	/**
	 * 消息类型
	 */
	private final Class<T> messageType;

	/**
	 * topic
	 */
	private final String topic;

	@SneakyThrows
	protected AbstractTopicMessageListener() {
		this.messageType = (Class<T>) TypeUtil.getTypeArgument(getClass(), 0);
		this.topic = messageType.newInstance().getTopic();
	}

	@Override
	public void onMessage(Message<T> message) {
		this.onMessage(message.getMessageObject());
	}

}
