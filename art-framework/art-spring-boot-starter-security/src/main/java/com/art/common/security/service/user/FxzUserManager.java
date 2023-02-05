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

package com.art.common.security.service.user;

import com.art.common.core.constant.SecurityConstants;
import com.art.common.core.exception.FxzException;
import com.art.common.core.model.ResultOpt;
import com.art.system.api.menu.MenuServiceApi;
import com.art.system.api.user.UserServiceApi;
import com.art.system.api.user.dto.SystemUserDTO;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 15:53
 */
@Service
@RequiredArgsConstructor
public class FxzUserManager {

	private final UserServiceApi userService;

	private final MenuServiceApi menuService;

	public SystemUserDTO findByName(String username) {
		// @formatter:off
		return ResultOpt.ofNullable(userService.findByName(username, SecurityConstants.FROM_IN))
				.assertSuccess(r -> new FxzException(String.format("根据用户名查询用户失败:%s,%s", username, r.getMsg())))
				.peek().getData();
		// @formatter:on
	}

	public SystemUserDTO findByMobile(String mobile) {
		// @formatter:off
		return ResultOpt.ofNullable(userService.findByMobile(mobile, SecurityConstants.FROM_IN))
				.assertSuccess(r -> new FxzException(String.format("根据手机号查询用户失败:%s,%s", mobile, r.getMsg())))
				.peek().getData();
		// @formatter:on
	}

	public String findUserPermissions(String username) {
		// @formatter:off
		Set<String> permissions = ResultOpt.ofNullable(menuService.findUserPermissions(username, SecurityConstants.FROM_IN))
				.assertSuccess(r -> new FxzException(String.format("查询用户权限失败:%s,%s", username, r.getMsg())))
				.peek().getData();
		// @formatter:on

		return String.join(StringPool.COMMA, permissions);
	}

}
