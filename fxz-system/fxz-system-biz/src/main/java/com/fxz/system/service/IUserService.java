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

package com.fxz.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.common.core.param.PageParam;
import com.fxz.system.dto.UserInfoDto;
import com.fxz.system.entity.SystemUser;
import com.fxz.system.entity.UserInfo;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 18:44
 */
public interface IUserService extends IService<SystemUser> {

	/**
	 * 查找用户详细信息
	 * @param page request
	 * @param user 用户查询对象，用于传递查询条件
	 * @return IPage
	 */
	IPage<SystemUser> findUserDetail(UserInfoDto user, PageParam page);

	/**
	 * 新增用户
	 * @param user user
	 */
	SystemUser createUser(SystemUser user);

	/**
	 * 修改用户
	 * @param user user
	 */
	void updateUser(SystemUser user);

	/**
	 * 删除用户
	 * @param userIds 用户 id数组
	 */
	void deleteUsers(String[] userIds);

	/**
	 * 根据用户id获取用户信息
	 */
	SystemUser getUserById(Long id);

	/**
	 * 通过用户名查找用户信息
	 */
	SystemUser findByName(String username);

	/**
	 * 获取用户全部信息
	 */
	UserInfo findUserInfo(SystemUser systemUser);

	/**
	 * 通过手机号查找用户信息
	 * @param mobile 手机号
	 * @return 用户信息
	 */
	SystemUser findByMobile(String mobile);

}
