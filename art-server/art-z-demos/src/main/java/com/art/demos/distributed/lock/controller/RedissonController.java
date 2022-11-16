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

package com.art.demos.distributed.lock.controller;

import com.art.common.lock.annotation.DistributedLock;
import com.art.common.lock.constant.RedissonLockType;
import com.art.common.lock.entity.LockEntity;
import com.art.common.lock.factory.RedissonLockServiceFactory;
import com.art.common.lock.lockresolver.impl.ExpressionDistributedLockKeyResolver;
import com.art.common.lock.service.RedissonService;
import com.art.common.mp.result.Result;
import com.art.common.security.annotation.Ojbk;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/9/4 18:19
 */
@Slf4j
@RequestMapping("/redisson/demo")
@RestController
@RequiredArgsConstructor
public class RedissonController {

	private final RedissonService redissonService = RedissonLockServiceFactory.getLock(RedissonLockType.REENTRANT);

	private final RedissonService readService = RedissonLockServiceFactory.getLock(RedissonLockType.READ);

	private final RedissonService writeService = RedissonLockServiceFactory.getLock(RedissonLockType.WRITE);

	/**
	 * 测试声明式加锁
	 */
	@DistributedLock(lockName = "reentrant", lockType = RedissonLockType.REENTRANT)
	@Ojbk
	@SneakyThrows
	@GetMapping("/reentrantAnn")
	public Result<String> reentrantAnn() {
		log.info("抢到可重入锁：{}", Thread.currentThread().getId());
		TimeUnit.SECONDS.sleep(5);
		return Result.success(LocalDateTime.now().toString());
	}

	/**
	 * 测试声明式加锁 不指定锁名称
	 */
	@DistributedLock(lockType = RedissonLockType.REENTRANT)
	@Ojbk
	@SneakyThrows
	@GetMapping("/reentrantAnnNo")
	public Result<String> reentrantAnnNo() {
		log.info("抢到可重入锁：{}", Thread.currentThread().getId());
		TimeUnit.SECONDS.sleep(5);
		return Result.success(LocalDateTime.now().toString());
	}

	/**
	 * 测试声明式加锁 spel
	 */
	@DistributedLock(lockName = "#name", lockType = RedissonLockType.REENTRANT,
			lockResolver = ExpressionDistributedLockKeyResolver.class)
	@Ojbk
	@SneakyThrows
	@GetMapping("/reentrantAnnSpel")
	public Result<String> reentrantAnnSpel(String name) {
		log.info("抢到可重入锁：{}", Thread.currentThread().getId());
		log.info("name:{}", name);
		TimeUnit.SECONDS.sleep(5);
		return Result.success(LocalDateTime.now().toString());
	}

	/**
	 * 测试声明式加锁 读锁
	 */
	@DistributedLock(lockName = "#name", lockType = RedissonLockType.READ,
			lockResolver = ExpressionDistributedLockKeyResolver.class)
	@Ojbk
	@SneakyThrows
	@GetMapping("/readAnn")
	public Result<String> readAnn(String name) {
		log.info("抢到读锁：{}", Thread.currentThread().getId());
		log.info("name:{}", name);
		TimeUnit.SECONDS.sleep(5);
		return Result.success(LocalDateTime.now().toString());
	}

	/**
	 * 测试声明式加锁 写锁
	 */
	@DistributedLock(lockName = "#name", lockType = RedissonLockType.WRITE,
			lockResolver = ExpressionDistributedLockKeyResolver.class)
	@Ojbk
	@SneakyThrows
	@GetMapping("/writeAnn")
	public Result<String> writeAnn(String name) {
		log.info("抢到写读：{}", Thread.currentThread().getId());
		log.info("name:{}", name);
		TimeUnit.SECONDS.sleep(5);
		return Result.success(LocalDateTime.now().toString());
	}

	@Ojbk
	@SneakyThrows
	@GetMapping("/reentrant")
	public Result<String> reentrant() {
		if (redissonService.lock(new LockEntity().setLockName("reentrant"))) {
			log.info("抢到可重入锁：{}", Thread.currentThread().getId());

			TimeUnit.SECONDS.sleep(5);

			redissonService.unlock(new LockEntity().setLockName("reentrant"));
		}

		return Result.success(LocalDateTime.now().toString());
	}

	@Ojbk
	@SneakyThrows
	@GetMapping("/read")
	public Result<String> read() {
		if (readService.lock(new LockEntity().setLockName("read"))) {
			log.info("抢到读锁：{}", Thread.currentThread().getId());

			TimeUnit.SECONDS.sleep(5);

			readService.unlock(new LockEntity().setLockName("read"));
		}

		return Result.success(LocalDateTime.now().toString());
	}

	@Ojbk
	@SneakyThrows
	@GetMapping("/write")
	public Result<String> writeService() {
		if (writeService.lock(new LockEntity().setLockName("read"))) {
			log.info("抢到写锁：{}", Thread.currentThread().getId());

			TimeUnit.SECONDS.sleep(5);

			writeService.unlock(new LockEntity().setLockName("read"));
		}

		return Result.success(LocalDateTime.now().toString());
	}

}
