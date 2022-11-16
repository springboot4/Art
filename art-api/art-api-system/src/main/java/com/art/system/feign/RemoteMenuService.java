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

package com.art.system.feign;

import com.art.common.core.constant.FxzServerConstant;
import com.art.common.core.constant.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Set;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022-03-06 14:50
 */
@FeignClient(contextId = "remoteMenuService", value = FxzServerConstant.FXZ_SERVER_SYSTEM)
public interface RemoteMenuService {

	/**
	 * 通过用户名查询权限信息
	 * @param username 用户名称
	 * @return 权限信息
	 */
	@GetMapping("/menu/findUserPermissions/{username}")
	Set<String> findUserPermissions(@PathVariable("username") String username,
			@RequestHeader(SecurityConstants.FROM) String from);

}
