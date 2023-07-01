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

package com.art.redis.sdk.event;

import com.art.redis.common.event.consume.KeyDeletedEventMessageConsume;
import com.art.redis.common.event.listener.AbstractKeyDeletedEventMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * key删除事件监听
 *
 * @author Fxz
 * @version 1.0
 * @date 2023/1/15 20:55
 */
@Slf4j
public class DefaultKeyDeletedEventMessageListener extends AbstractKeyDeletedEventMessageListener {

	private List<KeyDeletedEventMessageConsume> keyDeletedEventMessageConsume;

	@Override
	public void onMessage(Message message, @Nullable byte[] pattern) {
		if (CollectionUtils.isEmpty(keyDeletedEventMessageConsume)) {
			return;
		}

		super.onMessage(message, pattern);

		String key = message.toString();
		for (KeyDeletedEventMessageConsume deletedEventMessageConsume : keyDeletedEventMessageConsume) {
			if (deletedEventMessageConsume.support(key)) {
				if (log.isDebugEnabled()) {
					log.debug("{} handle key deleted event,the deleted key is {}",
							deletedEventMessageConsume.getClass().getName(), key);
				}
				deletedEventMessageConsume.consume(key);
			}
		}
	}

	/**
	 * Creates new {@link KeyspaceEventMessageListener}.
	 * @param listenerContainer must not be {@literal null}.
	 * @param objectProvider {@link KeyDeletedEventMessageConsume}
	 */
	public DefaultKeyDeletedEventMessageListener(RedisMessageListenerContainer listenerContainer,
			ObjectProvider<List<KeyDeletedEventMessageConsume>> objectProvider) {
		super(listenerContainer);
		objectProvider.ifAvailable(consumer -> this.keyDeletedEventMessageConsume = new ArrayList<>(consumer));
	}

}
