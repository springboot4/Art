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

package com.art.system.service;

import com.art.system.api.user.dto.SystemUserDTO;
import com.art.system.api.user.dto.SystemUserPageDTO;
import com.art.system.api.user.dto.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 18:44
 */
public interface UserService {

	/**
	 * 分页
	 */
	IPage<SystemUserDTO> pageUser(SystemUserPageDTO userPageDTO);

	/**
	 * 新增用户
	 * @param user user
	 */
	SystemUserDTO createUser(SystemUserDTO user);

	/**
	 * 修改用户
	 * @param user user
	 */
	void updateUser(SystemUserDTO user);

	/**
	 * 修改用户基础信息
	 * @param user user
	 */
	void updateUserInfo(SystemUserDTO user);

	/**
	 * 删除用户
	 * @param userIds 用户 id数组
	 */
	void deleteUsers(String[] userIds);

	/**
	 * 根据用户id获取用户信息
	 */
	SystemUserDTO getUserById(Long id);

	/**
	 * 通过用户名查找用户信息
	 */
	SystemUserDTO findByName(String username);

	/**
	 * 获取用户全部信息
	 */
	UserInfo findUserInfo(SystemUserDTO user);

	/**
	 * 通过手机号查找用户信息
	 * @param mobile 手机号
	 * @return 用户信息
	 */
	SystemUserDTO findByMobile(String mobile);

	/**
	 * 获取当前租户下的所有用户数
	 * @return 当前租户下的所有用户数
	 */
	long count();

}
