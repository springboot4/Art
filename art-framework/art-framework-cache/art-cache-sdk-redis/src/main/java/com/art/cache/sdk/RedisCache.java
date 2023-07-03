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

package com.art.cache.sdk;

import com.art.cache.common.DistributedCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundHashOperations;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/7/3 20:00
 */
@RequiredArgsConstructor
public class RedisCache<T> implements DistributedCache<T> {

	private final BoundHashOperations<String, String, T> hashOps;

	/**
	 * 获取指定键的值
	 * @param key key
	 * @return value
	 */
	@Override
	public T get(String key) {
		return hashOps.get(key);
	}

	/**
	 * 添加，有值则不替换
	 * @param key key
	 * @param data data
	 * @return true or false
	 */
	@Override
	public boolean add(String key, T data) {
		if (Boolean.TRUE.equals(hashOps.hasKey(key))) {
			return false;
		}
		return set(key, data);
	}

	/**
	 * 添加，有值则不替换
	 * @param key key
	 * @param data data
	 * @param seconds 过期时间，单位秒
	 * @return true or false
	 */
	@Override
	public boolean add(String key, T data, int seconds) {
		if (Boolean.TRUE.equals(hashOps.hasKey(key))) {
			return false;
		}
		return set(key, data, seconds);
	}

	/**
	 * 添加，有值则替换
	 * @param key key
	 * @param data data
	 * @return true or false
	 */
	@Override
	public boolean set(String key, T data) {
		try {
			hashOps.put(key, data);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 添加，有值则替换
	 * @param key 键
	 * @param data data
	 * @param seconds 过期时间，单位秒
	 * @return true or false
	 */
	@Override
	public boolean set(String key, T data, int seconds) {
		throw new RuntimeException("不支持的操作");
	}

	/**
	 * 删除
	 * @param key
	 * @return true or false
	 */
	@Override
	public boolean delete(String key) {
		try {
			hashOps.delete(key);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 替换，数据不存在返回false
	 * @param key key
	 * @param data data
	 * @return true or false
	 */
	@Override
	public boolean replace(String key, T data) {
		if (Boolean.FALSE.equals(hashOps.hasKey(key))) {
			return false;
		}
		return set(key, data);
	}

	/**
	 * 替换，数据不存在返回false
	 * @param key key
	 * @param data data
	 * @param seconds 过期时间，单位秒
	 * @return true or false
	 */
	@Override
	public boolean replace(String key, T data, int seconds) {
		return set(key, data, seconds);
	}

	/**
	 * 尝试加锁
	 * @param key key
	 * @return true or false
	 */
	@Override
	public boolean tryLock(String key) {
		throw new RuntimeException("不支持的操作");
	}

	/**
	 * 尝试加锁
	 * @param key key
	 * @param time 尝试加锁时间
	 * @return true or false
	 */
	@Override
	public boolean tryLock(String key, long time) {
		throw new RuntimeException("不支持的操作");
	}

	/**
	 * 尝试加锁
	 * @param key key
	 * @param seconds 尝试加锁时间
	 * @param leaseSeconds 加锁时间
	 * @return true or false
	 */
	@Override
	public boolean tryLock(String key, long seconds, long leaseSeconds) {
		throw new RuntimeException("不支持的操作");
	}

	/**
	 * 加锁
	 * @param key key
	 * @param seconds 过期时间，单位秒
	 */
	@Override
	public void lock(String key, long seconds) {
		throw new RuntimeException("不支持的操作");
	}

	/**
	 * 解锁
	 * @param key key
	 */
	@Override
	public void unlock(String key) {
		throw new RuntimeException("不支持的操作");
	}

	/**
	 * 无时间限制锁
	 * @param key key
	 */
	@Override
	public void lockInfinite(String key) {
		throw new RuntimeException("不支持的操作");
	}

	/**
	 * 是否加锁成功
	 * @param key key
	 * @return true or false
	 */
	@Override
	public boolean isLocked(String key) {
		throw new RuntimeException("不支持的操作");
	}

	/**
	 * 为指定key设置过期时间
	 * @param key 指定key
	 * @param seconds 过期时间，以秒为单位
	 * @return true or false
	 */
	@Override
	public boolean touch(String key, int seconds) {
		throw new RuntimeException("不支持的操作");
	}

}
