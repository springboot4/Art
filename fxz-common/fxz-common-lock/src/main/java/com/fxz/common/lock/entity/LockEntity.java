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

package com.fxz.common.lock.entity;

import lombok.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/4 17:17
 */
@Data
public class LockEntity {

	/**
	 * 锁名称，适用于公平锁、读写锁、重入锁
	 */
	private String lockName;

	/**
	 * 尝试加锁等待时间
	 */
	private long waitTime = 60L;

	/**
	 * 上锁时间
	 */
	private long leaseTime = 60L;

	/**
	 * 时间单位,默认为秒
	 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	/**
	 * 多个key集合,适用于连锁、红锁
	 */
	private List<String> keyList;

}
