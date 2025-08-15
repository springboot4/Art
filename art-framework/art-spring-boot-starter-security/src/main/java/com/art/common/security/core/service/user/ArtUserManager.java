/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.common.security.core.service.user;

import com.art.core.common.constant.SecurityConstants;
import com.art.core.common.exception.ArtException;
import com.art.core.common.model.ResultOpt;
import com.art.system.api.user.UserServiceApi;
import com.art.system.api.user.dto.SystemUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 15:53
 */
@Service
@RequiredArgsConstructor
public class ArtUserManager {

	private final UserServiceApi userService;

	public SystemUserDTO findByName(String username) {
		// @formatter:off
		return ResultOpt.ofNullable(userService.findByName(username, SecurityConstants.FROM_IN))
				.assertSuccess(r -> new ArtException(String.format("根据用户名查询用户失败:%s,%s", username, r.getMsg())))
				.peek().getData();
		// @formatter:on
	}

	public SystemUserDTO findByMobile(String mobile) {
		// @formatter:off
		return ResultOpt.ofNullable(userService.findByMobile(mobile, SecurityConstants.FROM_IN))
				.assertSuccess(r -> new ArtException(String.format("根据手机号查询用户失败:%s,%s", mobile, r.getMsg())))
				.peek().getData();
		// @formatter:on
	}

}
