package com.fxz.serversystem.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fxz.common.core.entity.FxzResponse;
import com.fxz.common.core.entity.PageParam;
import com.fxz.common.core.entity.system.SystemUser;
import com.fxz.common.core.exception.FxzException;
import com.fxz.common.core.utils.FxzUtil;
import com.fxz.serversystem.param.UserInfoParam;
import com.fxz.serversystem.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

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
	public FxzResponse getUserById(@PathVariable("id") Long id) {
		return new FxzResponse().data(userService.getUserById(id));
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('sys:user:view')")
	public FxzResponse userList(PageParam pageParam, UserInfoParam userInfoParam) {
		Map<String, Object> dataTable = FxzUtil.getDataTable(userService.findUserDetail(userInfoParam, pageParam));
		return new FxzResponse().data(dataTable);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('sys:user:add')")
	public void addUser(@RequestBody SystemUser user) throws FxzException {
		try {
			this.userService.createUser(user);
		}
		catch (Exception e) {
			String message = "新增用户失败";
			log.error(message, e);
			throw new FxzException(message);
		}
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('sys:user:update')")
	public void updateUser(@RequestBody SystemUser user) throws FxzException {
		try {
			this.userService.updateUser(user);
		}
		catch (Exception e) {
			String message = "修改用户失败";
			log.error(message, e);
			throw new FxzException(message);
		}
	}

	@DeleteMapping("/{userIds}")
	@PreAuthorize("hasAnyAuthority('sys:user:delete')")
	public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws FxzException {
		try {
			String[] ids = userIds.split(StringPool.COMMA);
			this.userService.deleteUsers(ids);
		}
		catch (Exception e) {
			String message = "删除用户失败";
			log.error(message, e);
			throw new FxzException(message);
		}
	}

}