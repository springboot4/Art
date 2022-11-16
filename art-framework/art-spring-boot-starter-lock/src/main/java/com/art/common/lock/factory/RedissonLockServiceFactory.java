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

package com.art.common.lock.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.art.common.lock.constant.RedissonLockType;
import com.art.common.lock.service.RedissonService;

import java.util.Map;

/**
 * redisson锁工厂
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/4 16:37
 */
public class RedissonLockServiceFactory {

	/**
	 * 根据类型获取不同的锁实现
	 * @param redissonLockType 锁类型
	 */
	public static RedissonService getLock(RedissonLockType redissonLockType) {
		Map<String, RedissonService> serviceMap = SpringUtil.getBeansOfType(RedissonService.class);
		return serviceMap.values().stream().filter(s -> redissonLockType.equals(s.lockType())).findFirst().orElse(null);
	}

}
