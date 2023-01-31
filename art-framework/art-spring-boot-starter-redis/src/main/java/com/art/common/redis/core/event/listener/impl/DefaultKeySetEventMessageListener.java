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

package com.art.common.redis.core.event.listener.impl;

import com.art.common.redis.core.event.consume.base.KeySetEventMessageConsume;
import com.art.common.redis.core.event.listener.base.AbstractKeySetEventMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * key set事件监听
 *
 * @author Fxz
 * @version 1.0
 * @date 2023/1/15 21:18
 */
@Slf4j
public class DefaultKeySetEventMessageListener extends AbstractKeySetEventMessageListener {

	private List<KeySetEventMessageConsume> keySetEventMessageConsumes;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		if (CollectionUtils.isEmpty(keySetEventMessageConsumes)) {
			return;
		}

		super.onMessage(message, pattern);

		String key = message.toString();
		for (KeySetEventMessageConsume keySetEventMessageConsume : keySetEventMessageConsumes) {
			if (keySetEventMessageConsume.support(key)) {
				if (log.isDebugEnabled()) {
					log.debug("{} handle key set event,the set key is {}",
							keySetEventMessageConsume.getClass().getName(), key);
				}
				keySetEventMessageConsume.consume(key);
			}
		}
	}

	/**
	 * Creates new {@link KeyspaceEventMessageListener}.
	 * @param listenerContainer must not be {@literal null}.
	 */
	public DefaultKeySetEventMessageListener(RedisMessageListenerContainer listenerContainer,
			ObjectProvider<List<KeySetEventMessageConsume>> objectProvider) {
		super(listenerContainer);
		objectProvider.ifAvailable(consumer -> this.keySetEventMessageConsumes = new ArrayList<>(consumer));
	}

}
