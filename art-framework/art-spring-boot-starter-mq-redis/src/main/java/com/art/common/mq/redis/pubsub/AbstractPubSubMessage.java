/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.common.mq.redis.pubsub;

import com.art.common.mq.redis.message.AbstractRedisMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 发布订阅消息抽象类
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/27 21:46
 */
public abstract class AbstractPubSubMessage extends AbstractRedisMessage {

	/**
	 * 获取发布订阅的主题
	 * @return 发布订阅的主题
	 */
	@JsonIgnore
	public abstract String getTopic();

}
