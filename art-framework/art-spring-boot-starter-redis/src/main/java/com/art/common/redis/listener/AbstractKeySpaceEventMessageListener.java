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

package com.art.common.redis.listener;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.lang.NonNull;

/**
 * @author Fxz
 * @version 1.0
 * @date 2023/1/15 20:41
 */
public abstract class AbstractKeySpaceEventMessageListener extends KeyspaceEventMessageListener
		implements ApplicationEventPublisherAware {

	protected ApplicationEventPublisher publisher;

	/**
	 * Register instance within the container.
	 * @param listenerContainer never {@literal null}.
	 */
	@Override
	protected void doRegister(@NonNull RedisMessageListenerContainer listenerContainer) {
		listenerContainer.addMessageListener(this, getKeyEventTopic());
	}

	/**
	 * Creates new {@link Topic} for listening
	 * @return Topic
	 */
	protected abstract Topic getKeyEventTopic();

	@Override
	protected void doHandleMessage(Message message) {
		publishEvent(new RedisKeyExpiredEvent(message.getBody()));
	}

	/**
	 * Publish the event in case an {@link ApplicationEventPublisher} is set.
	 * @param event can be {@literal null}.
	 */
	protected void publishEvent(RedisKeyExpiredEvent event) {
		if (publisher != null) {
			this.publisher.publishEvent(event);
		}
	}

	/**
	 * Set the ApplicationEventPublisher that this object runs in.
	 * <p>
	 * Invoked after population of normal bean properties but before an init callback like
	 * InitializingBean's afterPropertiesSet or a custom init-method. Invoked before
	 * ApplicationContextAware setApplicationContext.
	 * @param publisher event publisher to be used by this object
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	/**
	 * Creates new {@link KeyspaceEventMessageListener}.
	 * @param listenerContainer must not be {@literal null}.
	 */
	public AbstractKeySpaceEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

}
