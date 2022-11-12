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

package com.fxz.common.mq.redis.message;

import java.util.HashMap;
import java.util.Map;

/**
 * redis 消息抽象基类
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/6/30 16:16
 */
public abstract class AbstractRedisMessage {

	private Map<String, String> headers = new HashMap<>();

	public String getHeader(String key) {
		return headers.get(key);
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

}
