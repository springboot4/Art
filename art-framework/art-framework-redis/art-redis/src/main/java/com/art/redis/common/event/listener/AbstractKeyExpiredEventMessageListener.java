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

package com.art.redis.common.event.listener;

import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

/**
 * key 过期事件监听
 *
 * @author Fxz
 * @version 1.0
 * @date 2023/1/15 20:50
 */
public abstract class AbstractKeyExpiredEventMessageListener extends AbstractKeySpaceEventMessageListener {

	private static final Topic KEY_EVENT_EXPIRED_TOPIC = new PatternTopic("__keyevent@*__:expired");

	@Override
	public Topic getKeyEventTopic() {
		return KEY_EVENT_EXPIRED_TOPIC;
	}

	/**
	 * Creates new {@link KeyspaceEventMessageListener}.
	 * @param listenerContainer must not be {@literal null}.
	 */
	public AbstractKeyExpiredEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

}
