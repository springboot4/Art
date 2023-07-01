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

package com.art.system.manager;

import com.art.system.api.gitee.dto.UsersGiteeDTO;
import com.art.system.api.user.dto.SystemUserDTO;
import com.art.system.core.convert.UserConvert;
import com.art.system.core.convert.UsersGiteeConvert;
import com.art.system.dao.dataobject.UsersGiteeDO;
import com.art.system.dao.mysql.UsersGiteeMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 码云Gitee用户表
 *
 * @author fxz
 * @date 2023-04-16
 */
@Component
@RequiredArgsConstructor
public class UsersGiteeManager {

	private final UsersGiteeMapper sysUsersGiteeMapper;

	/**
	 * 新增
	 * @param sysUsersGiteeDTO sysUsersGiteeDTO
	 * @return 影响条数
	 */
	public Integer addSysUsersGitee(UsersGiteeDTO sysUsersGiteeDTO) {
		return sysUsersGiteeMapper.insert(UsersGiteeConvert.INSTANCE.convert(sysUsersGiteeDTO));
	}

	/**
	 * 根据id查询
	 * @param id 主键
	 * @return SysUsersGiteeDO
	 */
	public UsersGiteeDO findById(Long id) {
		return sysUsersGiteeMapper.selectById(id);
	}

	public UsersGiteeDO getByAppidAndId(String appId, Integer id) {
		return sysUsersGiteeMapper.selectOne(
				Wrappers.<UsersGiteeDO>lambdaQuery().eq(UsersGiteeDO::getAppid, appId).eq(UsersGiteeDO::getId, id));
	}

	/**
	 * 绑定用户
	 */
	public Integer binding(UsersGiteeDTO sysUsersGiteeDTO) {
		UsersGiteeDO convert = UsersGiteeConvert.INSTANCE.convert(sysUsersGiteeDTO);
		LambdaUpdateWrapper<UsersGiteeDO> wrapper = Wrappers.<UsersGiteeDO>lambdaUpdate()
			.eq(UsersGiteeDO::getId, sysUsersGiteeDTO.getId())
			.eq(UsersGiteeDO::getAppid, sysUsersGiteeDTO.getAppid())
			.set(UsersGiteeDO::getUsersId, sysUsersGiteeDTO.getUsersId())
			.set(UsersGiteeDO::getBindingDate, LocalDateTime.now());
		return sysUsersGiteeMapper.update(convert, wrapper);
	}

	/**
	 * 获取绑定的用户信息
	 */
	public SystemUserDTO getUser(String appId, Integer id) {
		return UserConvert.INSTANCE.convert(sysUsersGiteeMapper.getUser(appId, id));
	}

	/**
	 * 更新
	 */
	public Integer update(UsersGiteeDTO sysUsersGiteeDTO) {
		return sysUsersGiteeMapper.updateById(UsersGiteeConvert.INSTANCE.convert(sysUsersGiteeDTO));
	}

	/**
	 * 获取制定系统用户的绑定信息
	 * @param userId 系统用户id
	 * @return 绑定信息
	 */
	public UsersGiteeDO getBindInfo(Long userId) {
		return sysUsersGiteeMapper.selectOne(Wrappers.<UsersGiteeDO>lambdaQuery().eq(UsersGiteeDO::getUsersId, userId));
	}

	/**
	 * 解除绑定
	 * @param userId
	 */
	public void unBind(Long userId) {
		UsersGiteeDO usersGiteeDO = new UsersGiteeDO();
		usersGiteeDO.setUsersId(userId);

		sysUsersGiteeMapper.update(usersGiteeDO,
				Wrappers.<UsersGiteeDO>lambdaUpdate()
					.eq(UsersGiteeDO::getUsersId, userId)
					.set(UsersGiteeDO::getUsersId, null));
	}

}