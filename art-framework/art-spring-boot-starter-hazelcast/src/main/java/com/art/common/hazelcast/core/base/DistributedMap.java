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

package com.art.common.hazelcast.core.base;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 17:22
 */
public interface DistributedMap<K, V> {

	/**
	 * 添加键值对
	 * @param k key
	 * @param v value
	 */
	void put(K k, V v);

	/**
	 * 获取指定key对应的值
	 * @param k key
	 * @return value
	 */
	V get(K k);

	/**
	 * 替换值
	 * @param k key
	 * @param v value
	 * @return
	 */
	V replace(K k, V v);

	/**
	 * 所有的值集合
	 * @return values
	 */
	Collection<V> values();

	/**
	 * 返回此映射中包含的映射的不可变集克隆
	 * @return keys
	 */
	Set<Map.Entry<K, V>> entrySet();

	/**
	 * 清空集合
	 */
	void clear();

}
