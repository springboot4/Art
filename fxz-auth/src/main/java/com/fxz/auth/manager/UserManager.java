package com.fxz.auth.manager;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fxz.system.entity.SystemUser;
import com.fxz.system.feign.RemoteMenuService;
import com.fxz.system.feign.RemoteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 15:53
 */
@Service
@RequiredArgsConstructor
public class UserManager {

	private final RemoteUserService userService;

	private final RemoteMenuService menuService;

	public SystemUser findByName(String username) {
		return userService.findByName(username);
	}

	public String findUserPermissions(String username) {
		Set<String> userPermissions = menuService.findUserPermissions(username);
		return String.join(StringPool.COMMA, userPermissions);
	}

}
