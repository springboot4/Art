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

import com.art.common.redis.core.event.listener.base.AbstractKeyExpiredEventMessageListener;
import com.art.common.redis.core.event.consume.base.KeyExpiredEventMessageConsume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * key过期事件监听
 *
 * @author Fxz
 * @version 1.0
 * @date 2023/1/15 21:14
 */
@Slf4j
public class DefaultKeyExpiredEventMessageListener extends AbstractKeyExpiredEventMessageListener {

	private final List<KeyExpiredEventMessageConsume> keyExpiredEventMessageConsumes;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		if (CollectionUtils.isEmpty(keyExpiredEventMessageConsumes)) {
			return;
		}

		super.onMessage(message, pattern);

		String key = message.toString();
		for (KeyExpiredEventMessageConsume expiredEventMessageConsume : keyExpiredEventMessageConsumes) {
			if (expiredEventMessageConsume.support(key)) {
				if (log.isDebugEnabled()) {
					log.debug("{} handle key expired event,the expired key is {}",
							expiredEventMessageConsume.getClass().getName(), key);
				}
				expiredEventMessageConsume.consume(key);
			}
		}
	}

	/**
	 * Creates new {@link KeyspaceEventMessageListener}.
	 * @param listenerContainer must not be {@literal null}.
	 */
	public DefaultKeyExpiredEventMessageListener(RedisMessageListenerContainer listenerContainer,
			@Autowired(required = false) List<KeyExpiredEventMessageConsume> keyExpiredEventMessageConsumes) {
		super(listenerContainer);
		this.keyExpiredEventMessageConsumes = keyExpiredEventMessageConsumes;
	}

}
