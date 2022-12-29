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

package com.art.demos.core.distributed.lock.redis;

import cn.hutool.core.text.StrPool;
import com.art.demos.core.distributed.lock.redis.constant.RedisConstant;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * redis实现分布式锁demo
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/3 15:07
 */
public class DistributedRedisLock implements Lock {

	/**
	 * redisTemplate
	 */
	private final StringRedisTemplate redisTemplate;

	/**
	 * 锁名称
	 */
	private final String lockName;

	/**
	 * 获取锁的线程id
	 */
	private final String threadId;

	/**
	 * 锁过期时间
	 */
	private long expire = 30;

	public DistributedRedisLock(StringRedisTemplate redisTemplate, String lockName, String uuid) {
		this.lockName = lockName;
		this.redisTemplate = redisTemplate;
		threadId = Thread.currentThread().getId() + StrPool.C_COLON + uuid;
	}

	@Override
	public void lock() {
		this.tryLock();
	}

	@Override
	public boolean tryLock() {
		return this.tryLock(-1L, TimeUnit.SECONDS);
	}

	/**
	 * 加锁方法
	 * @param time the maximum time to wait for the lock 等待锁的最长时间
	 * @param unit the time unit of the {@code time} argument 参数的时间单位
	 * @return true or false
	 */
	@SneakyThrows
	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		if (!unit.equals(TimeUnit.SECONDS)) {
			expire = unit.toSeconds(time);
		}

		while (Boolean.FALSE.equals(
				this.redisTemplate.execute(new DefaultRedisScript<>(RedisConstant.LOCK_LUA_SCRIPT, Boolean.class),
						Collections.singletonList(lockName), threadId, String.valueOf(expire)))) {
			TimeUnit.MILLISECONDS.sleep(30);
		}

		// 加锁成功，开启定时器自动续期
		this.renewExpire();

		return true;
	}

	/**
	 * 解锁
	 */
	@Override
	public void unlock() {
		if (Objects.isNull(
				this.redisTemplate.execute(new DefaultRedisScript<>(RedisConstant.UNLOCK_LUA_SCRIPT, Long.class),
						Collections.singletonList(lockName), threadId))) {
			throw new IllegalMonitorStateException("this lock doesn't belong to you!");
		}
	}

	/**
	 * 续期
	 */
	private void renewExpire() {
		// 每隔三分之一的过期时间对key进行续期
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (Boolean.TRUE.equals(redisTemplate.execute(
						new DefaultRedisScript<>(RedisConstant.RENEWEXPIRE_LUA_SCRIPT, Boolean.class),
						Collections.singletonList(lockName), threadId, String.valueOf(expire)))) {
					renewExpire();
				}
			}
		}, this.expire * 1000 / 3);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
	}

	@Override
	public Condition newCondition() {
		return null;
	}

}
