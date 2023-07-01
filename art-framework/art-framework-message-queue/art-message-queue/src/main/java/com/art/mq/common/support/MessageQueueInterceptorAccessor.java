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

import java.util.ArrayList;
import java.util.List;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/6/30 14:36
 */
public abstract class MessageQueueInterceptorAccessor<T extends MessageQueueInterceptor> {

	/**
	 * 消息处理拦截器
	 */
	private final List<T> interceptors = new ArrayList<>();

	/**
	 * 消息发送前处理
	 */
	protected void sendMessageBefore(AbstractMessage message) {
		interceptors.forEach(interceptor -> interceptor.sendMessageBefore(message));
	}

	/**
	 * 消息发送后处理
	 */
	protected void sendMessageAfter(AbstractMessage message) {
		for (int i = interceptors.size() - 1; i >= 0; i--) {
			interceptors.get(i).sendMessageAfter(message);
		}
	}

	/**
	 * 获取拦截器
	 */
	public List<T> getInterceptors() {
		return interceptors;
	}

	/**
	 * 添加拦截器
	 */
	public void addInterceptor(List<T> messageQueueInterceptors) {
		interceptors.addAll(messageQueueInterceptors);
	}

}
