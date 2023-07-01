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

package com.art.system.service.impl;

import com.art.common.security.core.model.ArtAuthUser;
import com.art.common.security.core.utils.SecurityUtil;
import com.art.system.api.gitee.dto.UsersGiteeDTO;
import com.art.system.api.third.dto.ThirdBindInfo;
import com.art.system.api.user.dto.SystemUserDTO;
import com.art.system.core.ThirdTypeEnums;
import com.art.system.core.convert.UsersGiteeConvert;
import com.art.system.dao.dataobject.UsersGiteeDO;
import com.art.system.manager.UsersGiteeManager;
import com.art.system.service.ThirdOperationService;
import com.art.system.service.UsersGiteeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 码云Gitee用户表
 *
 * @author fxz
 * @date 2023-04-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsersGiteeServiceImpl implements UsersGiteeService, ThirdOperationService {

	private final UsersGiteeManager sysUsersGiteeManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addSysUsersGitee(UsersGiteeDTO sysUsersGiteeDTO) {
		return sysUsersGiteeManager.addSysUsersGitee(sysUsersGiteeDTO) > 0;
	}

	/**
	 * 获取单条
	 */
	@Override
	public UsersGiteeDTO findById(Long id) {
		return UsersGiteeConvert.INSTANCE.convert(sysUsersGiteeManager.findById(id));
	}

	/**
	 * 获取制定系统用户的绑定信息
	 * @param userId 系统用户id
	 * @return 绑定信息
	 */
	@Override
	public ThirdBindInfo getBindInfo(Long userId) {
		UsersGiteeDO giteeDO = sysUsersGiteeManager.getBindInfo(userId);

		ThirdBindInfo.ThirdBindInfoBuilder builder = ThirdBindInfo.builder();
		if (Objects.nonNull(giteeDO)) {
			builder.bind(true).username(giteeDO.getName()).avatar(giteeDO.getAvatarUrl());
		}
		return builder.build();
	}

	/**
	 * @param appId 应用id
	 * @param id gitee用户唯一标识
	 * @return
	 */
	@Override
	public UsersGiteeDTO getByAppidAndId(String appId, Integer id) {
		return UsersGiteeConvert.INSTANCE.convert(sysUsersGiteeManager.getByAppidAndId(appId, id));
	}

	/**
	 * 绑定用户
	 * @param sysUsersGiteeDTO
	 */
	@Override
	public Boolean binding(UsersGiteeDTO sysUsersGiteeDTO) {
		return sysUsersGiteeManager.binding(sysUsersGiteeDTO) > 0;
	}

	/**
	 * @param appId
	 * @param id
	 * @return
	 */
	@Override
	public SystemUserDTO getUser(String appId, Integer id) {
		return sysUsersGiteeManager.getUser(appId, id);
	}

	/**
	 * 更新
	 */
	@Override
	public Boolean update(UsersGiteeDTO sysUsersGiteeDTO) {
		return sysUsersGiteeManager.update(sysUsersGiteeDTO) > 0;
	}

	/**
	 * 解除绑定
	 */
	@Override
	public void unBind() {
		ArtAuthUser user = SecurityUtil.getUser();
		sysUsersGiteeManager.unBind(user.getUserId());
	}

	@Override
	public String support() {
		return ThirdTypeEnums.GITEE.getValue();
	}

}