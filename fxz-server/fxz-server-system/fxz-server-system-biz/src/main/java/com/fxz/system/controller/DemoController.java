package com.fxz.system.controller;

import com.fxz.common.core.result.Result;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.common.security.util.SecurityUtil;
import com.fxz.system.dinger.UserDinger;
import com.fxz.system.entity.SystemUser;
import com.github.jaemon.dinger.core.entity.DingerResponse;
import com.github.jaemon.dinger.core.entity.ImageTextDeo;
import com.github.jaemon.dinger.core.entity.LinkDeo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-19 20:23
 */
@RequestMapping("/dinger")
@RequiredArgsConstructor
@RestController
@Slf4j
public class DemoController {

	private final UserDinger userDinger;

	private final RedissonClient redissonClient;

	@Cacheable(value = "fxz_cloud", key = "#userName", unless = "#result==null")
	@GetMapping("/cache/ann")
	public FxzAuthUser cacheAnn(String userName) {
		FxzAuthUser user = SecurityUtil.getUser();
		log.info("user:{}", user);
		return user;
	}

	@GetMapping("/redisson/test")
	public Result<Void> redisson() {
		RLock lock = redissonClient.getLock("test");
		// 最多等待10s 上锁成功后10s解锁
		try {
			boolean result = lock.tryLock(10, 10, TimeUnit.SECONDS);
			if (result) {
				log.info("强锁成功,线程:{},当前时间:{}", Thread.currentThread().getId(), LocalDateTime.now());
				TimeUnit.SECONDS.sleep(9L);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			lock.unlock();
		}
		return Result.success();
	}

	@GetMapping("/link")
	public Result<DingerResponse> link(String url) {
		return Result.success(
				userDinger.link(LinkDeo.instance("邦邦两拳", "..........", "https://gitee.com/BangBangTwoFists", url)));
	}

	@GetMapping("/imageText")
	public Result<DingerResponse> imageText() {
		List<ImageTextDeo> list = new ArrayList<>();
		list.add(ImageTextDeo.instance("邦邦两拳", "https://gitee.com/BangBangTwoFists",
				"https://portrait.gitee.com/uploads/avatars/namespace/2742/8226626_BangBangTwoFists_1647526759.png!avatar100"));
		return Result.success(userDinger.imageText(list));
	}

	@GetMapping("/success/{username}/{phones}")
	public Result<DingerResponse> success(@PathVariable("username") String username,
			@PathVariable("phones") String phones) {
		return Result.success(userDinger.success(LocalDateTime.now().toString(), phones));
	}

	@GetMapping("/failed/{username}")
	public Result<DingerResponse> failed(@PathVariable("username") String username) {
		return Result.success(userDinger.failed(1, username));
	}

	@GetMapping("/login/{username}")
	public Result<DingerResponse> login(@PathVariable("username") String username) {
		return Result.success(userDinger.login(username));
	}

	@GetMapping("/logout/{username}")
	public Result<DingerResponse> logout(@PathVariable("username") String username) {
		return Result.success(userDinger.logout(1, username));
	}

}
