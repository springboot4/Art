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

package com.art.hazelcast.sdk.base;

import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2023/3/23 17:22
 */
public interface DistributedCountDownLatch {

	/**
	 * 等待
	 * @param timeout 等待最大时长
	 * @param unit 时间单位
	 * @return true or false
	 * @throws InterruptedException InterruptedException
	 */
	boolean await(long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * 计数器-1
	 */
	void countDown();

	/**
	 * getCount
	 * @return count
	 */
	int getCount();

	/**
	 * 尝试设置计数器数值，0时成功
	 * @param count count
	 * @return true or false
	 */
	boolean trySetCount(int count);

	/**
	 * 销毁集合
	 */
	void destroy();

	/**
	 * 获取集合名称
	 * @return 集合名称
	 */
	String getName();

}
