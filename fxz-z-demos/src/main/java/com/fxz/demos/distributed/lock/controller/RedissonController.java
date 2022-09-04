package com.fxz.demos.distributed.lock.controller;

import com.fxz.common.lock.constant.RedissonLockType;
import com.fxz.common.lock.entity.LockEntity;
import com.fxz.common.lock.factory.RedissonLockServiceFactory;
import com.fxz.common.lock.service.RedissonService;
import com.fxz.common.mp.result.Result;
import com.fxz.common.security.annotation.Ojbk;
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
 * @version 1.0
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
