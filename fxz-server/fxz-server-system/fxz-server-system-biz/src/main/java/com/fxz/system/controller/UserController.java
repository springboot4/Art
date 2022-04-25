package com.fxz.system.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fxz.common.core.exception.FxzException;
import com.fxz.common.core.param.PageParam;
import com.fxz.common.mp.result.PageResult;
import com.fxz.common.mp.result.Result;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.common.security.util.SecurityUtil;
import com.fxz.system.entity.SystemUser;
import com.fxz.system.entity.UserInfo;
import com.fxz.system.param.UserInfoParam;
import com.fxz.system.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @author fxz
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;

	@GetMapping("/getUserById/{id}")
	public Result<SystemUser> getUserById(@PathVariable("id") Long id) {
		return Result.success(userService.getUserById(id));
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('sys:user:view')")
	public Result<PageResult<SystemUser>> userList(PageParam pageParam, UserInfoParam userInfoParam) {
		return Result.success(PageResult.success(userService.findUserDetail(userInfoParam, pageParam)));
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('sys:user:add')")
	public void addUser(@RequestBody SystemUser user) {
		this.userService.createUser(user);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('sys:user:update')")
	public void updateUser(@RequestBody SystemUser user) {
		this.userService.updateUser(user);
	}

	@DeleteMapping("/{userIds}")
	@PreAuthorize("hasAnyAuthority('sys:user:delete')")
	public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws FxzException {
		String[] ids = userIds.split(StringPool.COMMA);
		this.userService.deleteUsers(ids);
	}

	/**
	 * 通过用户名查找用户信息
	 */
	@GetMapping("/findByName/{username}")
	public SystemUser findByName(@PathVariable("username") String username) {
		return this.userService.findByName(username);
	}

	/**
	 * 获取当前用户全部信息
	 */
	@GetMapping("/info")
	public Result<UserInfo> userInfo() {
		FxzAuthUser user = SecurityUtil.getUser();
		SystemUser systemUser = userService
				.getOne(Wrappers.<SystemUser>lambdaQuery().eq(SystemUser::getUsername, user.getUsername()));
		if (ObjectUtil.isNull(systemUser)) {
			return Result.failed();
		}
		return Result.success(userService.findUserInfo(systemUser));
	}

}