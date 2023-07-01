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

package com.art.mq.common.support;

import com.art.mq.common.message.AbstractBroadcastMessage;
import com.art.mq.common.message.AbstractGroupMessage;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/6/30 14:37
 */
public interface MessageQueueOperations {

	/**
	 * 发送分组消息，此消息对于同一个消费者组仅会被消费一次
	 */
	<T extends AbstractGroupMessage> void send(T message);

	/**
	 * 发送广播消息，此消息对于所有订阅的消费者都会收到
	 */
	<T extends AbstractBroadcastMessage> void send(T message);

}
