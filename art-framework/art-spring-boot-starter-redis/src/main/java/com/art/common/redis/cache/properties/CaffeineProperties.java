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

package com.art.common.redis.cache.properties;

import lombok.Data;

/**
 * @author fxz
 */
@Data
public class CaffeineProperties {

	/**
	 * 访问后过期时间，单位毫秒
	 */
	private long expireAfterAccess;

	/**
	 * 写入后过期时间，单位毫秒
	 */
	private long expireAfterWrite;

	/**
	 * 写入后刷新时间，单位毫秒
	 */
	private long refreshAfterWrite;

	/**
	 * 初始化大小
	 */
	private int initialCapacity;

	/**
	 * 最大缓存对象个数，超过此数量时之前放入的缓存将失效
	 */
	private long maximumSize;

}