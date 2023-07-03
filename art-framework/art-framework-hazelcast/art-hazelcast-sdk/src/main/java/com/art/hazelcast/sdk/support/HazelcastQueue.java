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

import com.art.hazelcast.sdk.base.DistributedQueue;
import com.hazelcast.collection.IQueue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 17:47
 */
@RequiredArgsConstructor
public class HazelcastQueue<T> implements DistributedQueue<T> {

	private final IQueue<T> queue;

	/**
	 * 添加
	 * @param data 数据
	 * @return true or false
	 */
	@Override
	public boolean offer(T data) {
		return queue.offer(data);
	}

	/**
	 * 检索并删除此队列的头，如果此队列为空，则返回null
	 * @param timeout 超时时间
	 * @param timeUnit 时间单位
	 * @return 数据
	 */
	@SneakyThrows
	@Override
	public T poll(long timeout, TimeUnit timeUnit) {
		return queue.poll(timeout, timeUnit);
	}

	/**
	 * 清空队列
	 */
	@Override
	public void clear() {
		queue.clear();
	}

}
