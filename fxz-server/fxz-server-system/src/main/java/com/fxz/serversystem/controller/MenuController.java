package com.fxz.serversystem.controller;

import cn.hutool.core.util.ObjectUtil;
import com.fxz.common.core.entity.FxzResponse;
import com.fxz.common.core.entity.router.VueRouter;
import com.fxz.common.core.entity.system.Menu;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.common.security.util.SecurityUtil;
import com.fxz.serversystem.service.IMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-29 18:47
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

	private final IMenuService menuService;

	/**
	 * 获取用户路由
	 */
	@GetMapping("/nav")
	public FxzResponse getUserRouters() {
		// 获取当前用户
		FxzAuthUser user = SecurityUtil.getUser();
		if (ObjectUtil.isEmpty(user)) {
			return null;
		}

		Map<String, Object> result = new HashMap<>();
		// 构建用户路由对象
		List<VueRouter<Menu>> userRouters = this.menuService.getUserRouters(user.getUsername());
		// 获取用户权限信息
		Set<String> userPermissions = this.menuService.findUserPermissions(user.getUsername());
		// 组装数据
		result.put("routes", userRouters);
		result.put("permissions", userPermissions);
		return new FxzResponse().data(result);
	}

}
