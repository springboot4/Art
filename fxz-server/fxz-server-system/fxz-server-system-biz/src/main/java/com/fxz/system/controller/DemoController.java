package com.fxz.system.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fxz.common.core.result.Result;
import com.fxz.system.dinger.UserDinger;
import com.github.jaemon.dinger.core.entity.DingerResponse;
import com.github.jaemon.dinger.core.entity.ImageTextDeo;
import com.github.jaemon.dinger.core.entity.LinkDeo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		return Result.success(userDinger.success(username, phones));
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
