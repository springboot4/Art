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

package com.art.mq.common.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 广播消息,此消息对于所有订阅的消费者都会收到
 *
 * @author fxz
 * @version 0.0.1
 * @date 2023/6/30 14:43
 */
public abstract class AbstractBroadcastMessage extends AbstractMessage {

	/**
	 * 获取发布订阅的主题
	 * @return 发布订阅的主题
	 */
	@JsonIgnore
	public abstract String getTopic();

}
