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

import com.art.hazelcast.sdk.base.DistributedCountDownLatch;
import com.hazelcast.cp.ICountDownLatch;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 17:43
 */
@RequiredArgsConstructor
public class HazelcastCountDownLatch implements DistributedCountDownLatch {

	private final ICountDownLatch countDownLatch;

	/**
	 * 等待
	 * @param timeout 等待最大时长
	 * @param unit 时间单位
	 * @return true or false
	 * @throws InterruptedException InterruptedException
	 */
	@Override
	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		return countDownLatch.await(timeout, unit);
	}

	/**
	 * 计数器-1
	 */
	@Override
	public void countDown() {
		countDownLatch.countDown();
	}

	/**
	 * getCount
	 * @return count
	 */
	@Override
	public int getCount() {
		return countDownLatch.getCount();
	}

	/**
	 * 尝试设置计数器数值，0时成功
	 * @param count count
	 * @return true or false
	 */
	@Override
	public boolean trySetCount(int count) {
		return countDownLatch.trySetCount(count);
	}

	/**
	 * 销毁集合
	 */
	@Override
	public void destroy() {
		countDownLatch.destroy();
	}

	/**
	 * 获取集合名称
	 * @return 集合名称
	 */
	@Override
	public String getName() {
		return countDownLatch.getName();
	}

}
