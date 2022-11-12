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

package com.fxz.common.mq.redis.stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fxz.common.mq.redis.message.AbstractRedisMessage;

/**
 * Redis Stream Message 抽象类
 *
 * @author fxz
 */
public abstract class AbstractStreamMessage extends AbstractRedisMessage {

	/**
	 * 获得 Redis Stream Key
	 * @return Channel
	 * @JsonIgnore 避免序列化。原因是，Redis 发布 Channel 消息的时候，已经会指定。
	 */
	@JsonIgnore
	public abstract String getStreamKey();

}
