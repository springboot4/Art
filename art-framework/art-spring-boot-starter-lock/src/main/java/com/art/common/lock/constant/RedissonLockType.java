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

package com.art.common.lock.constant;

import lombok.NoArgsConstructor;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/4 17:55
 */
@NoArgsConstructor
public enum RedissonLockType {

	/**
	 * 可重入锁
	 */
	REENTRANT,

	/**
	 * 公平锁
	 */
	FAIR,

	/**
	 * 联锁
	 */
	MULTI,

	/**
	 * 红锁
	 */
	RED,

	/**
	 * 读锁
	 */
	READ,

	/**
	 * 写锁
	 */
	WRITE;

}
