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

package com.fxz.system.feign;

import com.fxz.common.core.constant.FxzServerConstant;
import com.fxz.common.core.constant.SecurityConstants;
import com.fxz.system.entity.SystemUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022-03-06 14:50
 */
@FeignClient(contextId = "remoteUserService", value = FxzServerConstant.FXZ_SERVER_SYSTEM)
public interface RemoteUserService {

	/**
	 * 通过用户名查找用户信息
	 * @param username 用户名
	 * @return 用户信息
	 */
	@GetMapping("/user/findByName/{username}")
	SystemUser findByName(@PathVariable("username") String username,
			@RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过手机号查找用户信息
	 * @param mobile 手机号
	 * @return 用户信息
	 */
	@GetMapping("/user/findByMobile/{mobile}")
	SystemUser findByMobile(@PathVariable("mobile") String mobile, @RequestHeader(SecurityConstants.FROM) String from);

}
