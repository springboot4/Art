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

package com.art.cache.sdk.properties;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fxz
 */
@Data
public class RedisProperties {

	/**
	 * 全局过期时间，单位毫秒，默认10分钟
	 */
	private long defaultExpiration = 10 * 60 * 1000;

	/**
	 * 每个cacheName的过期时间，单位毫秒，优先级比defaultExpiration高
	 */
	private Map<String, Long> expires = new HashMap<>();

	/**
	 * 缓存更新时通知其他节点的topic名称
	 */
	private String topic = "cache:redis:caffeine:topic";

}