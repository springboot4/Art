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

package com.art.hazelcast.sdk.support;

import com.art.hazelcast.sdk.base.DistributedMap;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 17:55
 */
@RequiredArgsConstructor
public class HazelcastMap<K, V> implements DistributedMap<K, V> {

	private final IMap<K, V> map;

	/**
	 * 添加键值对
	 * @param k key
	 * @param v value
	 */
	@Override
	public void put(K k, V v) {
		map.put(k, v);
	}

	/**
	 * 获取指定key对应的值
	 * @param k key
	 * @return value
	 */
	@Override
	public V get(K k) {
		return map.get(k);
	}

	/**
	 * 替换值
	 * @param k key
	 * @param v value
	 * @return 旧值 or null
	 */
	@Override
	public V replace(K k, V v) {
		return map.replace(k, v);
	}

	/**
	 * 所有的值集合
	 * @return values
	 */
	@Override
	public Collection<V> values() {
		return map.values();
	}

	/**
	 * 返回此映射中包含的映射的不可变集克隆
	 * @return keys
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	/**
	 * 清空集合
	 */
	@Override
	public void clear() {
		map.clear();
	}

}
