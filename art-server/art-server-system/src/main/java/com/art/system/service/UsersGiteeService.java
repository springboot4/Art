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

package com.art.system.service;

import com.art.system.api.gitee.dto.UsersGiteeDTO;
import com.art.system.api.user.dto.SystemUserDTO;

/**
 * 码云Gitee用户表
 *
 * @author fxz
 * @date 2023-04-16
 */
public interface UsersGiteeService {

	/**
	 * 添加
	 */
	Boolean addSysUsersGitee(UsersGiteeDTO sysUsersGiteeDTO);

	/**
	 * 获取单条
	 */
	UsersGiteeDTO findById(Long id);

	/**
	 * 根据appid、id查询
	 * @param appId 应用id
	 * @param id gitee用户唯一标识
	 * @return UsersGiteeDTO
	 */
	UsersGiteeDTO getByAppidAndId(String appId, Integer id);

	/**
	 * 绑定用户
	 */
	Boolean binding(UsersGiteeDTO sysUsersGiteeDTO);

	/**
	 * 获取用户信息
	 * @param appId appId
	 * @param id id
	 * @return 绑定的用户详细信息
	 */
	SystemUserDTO getUser(String appId, Integer id);

	/**
	 * 更新
	 */
	Boolean update(UsersGiteeDTO sysUsersGiteeDTO);

}