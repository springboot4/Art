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

import com.art.mq.common.message.AbstractMessage;

/**
 * 消息拦截器
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/6/30 14:36
 */
public interface MessageQueueInterceptor {

	/**
	 * 发送前拦截
	 */
	void sendMessageBefore(AbstractMessage message);

	/**
	 * 发送后拦截
	 */
	void sendMessageAfter(AbstractMessage message);

	/**
	 * 消费前拦截
	 */
	void consumeMessageBefore(AbstractMessage message);

	/**
	 * 消费后拦截
	 */
	void consumeMessageAfter(AbstractMessage message);

}
