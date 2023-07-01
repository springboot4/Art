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

import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 17:22
 */
public interface DistributedQueue<T> {

	/**
	 * 添加
	 * @param data 数据
	 * @return true or false
	 */
	boolean offer(T data);

	/**
	 * 检索并删除此队列的头，如果此队列为空，则返回null
	 * @param timeout 超时时间
	 * @param timeUnit 时间单位
	 * @return 数据
	 */
	T poll(long timeout, TimeUnit timeUnit);

	/**
	 * 清空队列
	 */
	void clear();

}
