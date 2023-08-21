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

package com.art.system.api.gitee;

import com.art.core.common.constant.ArtServerConstants;
import com.art.core.common.constant.SecurityConstants;
import com.art.core.common.model.Result;
import com.art.system.api.gitee.dto.UsersGiteeDTO;
import com.art.system.api.user.dto.SystemUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/16 14:44
 */
@FeignClient(contextId = "userGiteeServiceApi", value = ArtServerConstants.ART_SERVER_SYSTEM)
public interface UserGiteeServiceApi {

	/**
	 * 添加
	 */
	@PostMapping(value = "/gitee/add", headers = SecurityConstants.HEADER_INNER)
	Result<Boolean> add(@RequestBody UsersGiteeDTO sysUsersGiteeDTO);

	/**
	 * 绑定用户
	 */
	@PostMapping(value = "/gitee/binding", headers = SecurityConstants.HEADER_INNER)
	public Result<Boolean> binding(@RequestBody UsersGiteeDTO sysUsersGiteeDTO);

	/**
	 * 根据appid、id查询
	 */
	@GetMapping(value = "/gitee/getByAppidAndId", headers = SecurityConstants.HEADER_INNER)
	Result<UsersGiteeDTO> getByAppidAndId(@RequestParam("appId") String appId, @RequestParam("id") Integer id);

	/**
	 * 获取绑定的用户信息
	 */
	@GetMapping(value = "/gitee/getUser", headers = SecurityConstants.HEADER_INNER)
	Result<SystemUserDTO> getUser(@RequestParam("appId") String appId, @RequestParam("id") Integer id);

	/**
	 * 更新
	 */
	@PostMapping(value = "/gitee/update", headers = SecurityConstants.HEADER_INNER)
	Result<Boolean> update(@RequestBody UsersGiteeDTO sysUsersGiteeDTO);

}
