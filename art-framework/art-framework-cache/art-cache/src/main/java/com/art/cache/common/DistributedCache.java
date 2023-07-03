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

package com.art.cache.common;

/**
 * 分布式缓存
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 15:40
 */
public interface DistributedCache<T> {

	/**
	 * 获取指定键的值
	 * @param key key
	 * @return value
	 */
	T get(String key);

	/**
	 * 为指定key设置过期时间
	 * @param key 指定key
	 * @param seconds 过期时间，以秒为单位
	 * @return true or false
	 */
	boolean touch(final String key, int seconds);

	/**
	 * 添加，有值则不替换
	 * @param key key
	 * @param data data
	 * @return true or false
	 */
	boolean add(String key, T data);

	/**
	 * 添加，有值则不替换
	 * @param key key
	 * @param data data
	 * @param seconds 过期时间，单位秒
	 * @return true or false
	 */
	boolean add(String key, T data, int seconds);

	/**
	 * 添加，有值则替换
	 * @param key key
	 * @param data data
	 * @return true or false
	 */
	boolean set(String key, T data);

	/**
	 * 添加，有值则替换
	 * @param key key
	 * @param data data
	 * @param seconds 过期时间，单位秒
	 * @return true or false
	 */
	boolean set(String key, T data, int seconds);

	/**
	 * 删除
	 * @param key
	 * @return true or false
	 */
	boolean delete(String key);

	/**
	 * 替换，数据不存在返回false
	 * @param key key
	 * @param data data
	 * @return true or false
	 */
	boolean replace(String key, T data);

	/**
	 * 替换，数据不存在返回false
	 * @param key key
	 * @param data data
	 * @param seconds 过期时间，单位秒
	 * @return true or false
	 */
	boolean replace(String key, T data, int seconds);

	/**
	 * 加锁
	 * @param key key
	 * @param seconds 过期时间，单位秒
	 */
	void lock(String key, long seconds);

	/**
	 * 解锁
	 * @param key key
	 */
	void unlock(String key);

	/**
	 * 无时间限制锁
	 * @param key key
	 */
	void lockInfinite(String key);

	/**
	 * 尝试加锁
	 * @param key key
	 * @return true or false
	 */
	boolean tryLock(String key);

	/**
	 * 尝试加锁
	 * @param key key
	 * @param seconds 尝试加锁时间
	 * @return true or false
	 */
	boolean tryLock(String key, long seconds);

	/**
	 * 尝试加锁
	 * @param key key
	 * @param seconds 尝试加锁时间
	 * @param leaseSeconds 加锁时间
	 * @return true or false
	 */
	boolean tryLock(String key, long seconds, long leaseSeconds);

	/**
	 * 是否加锁成功
	 * @param key key
	 * @return true or false
	 */
	boolean isLocked(String key);

}
