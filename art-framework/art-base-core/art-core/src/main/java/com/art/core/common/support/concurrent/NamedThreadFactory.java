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

package com.art.core.common.support.concurrent;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义 ThreadFactory 实现线程重命名
 * <p>
 * 线程池七大核心参数：
 * <p>
 * 1:corePoolSize：线程池中常驻核心线程数
 * <p>
 * 2:maximumPoolSize：线程池能够容纳同时执行的最大线程数，此值必须大于1
 * <p>
 * 3:keepAliveTime：多余空闲线程的存活时间。当前线程池数量超过corePoolSize时，当空闲时间达到keepAliveTime时，多余空闲线程会被销毁直到剩下corePoolSize为止。
 * <p>
 * 4:unit：keepAliveTime的单位
 * <p>
 * 5:workQueue：里面放了被提交但是尚未执行的任务
 * <p>
 * 6:threadFactory：表示线程池中工作线程的线程工厂，用于创建线程
 * <p>
 * 7:handler：拒绝策略，当队列满了并且工作线程大于等于线程池的最大线程数（maximumPoolSize）时，对任务的拒绝方式。
 *
 * @author fxz
 */
public class NamedThreadFactory implements ThreadFactory {

	private final AtomicInteger poolNumber = new AtomicInteger(1);

	private final ThreadGroup threadGroup;

	private final AtomicInteger threadNumber = new AtomicInteger(1);

	public final String namePrefix;

	public NamedThreadFactory(String name) {
		SecurityManager s = System.getSecurityManager();

		threadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

		if (Objects.isNull(name)) {
			name = "pool";
		}

		namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(threadGroup, r, namePrefix + threadNumber.getAndIncrement(), 0);
		t.setDaemon(false);
		t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}

}
