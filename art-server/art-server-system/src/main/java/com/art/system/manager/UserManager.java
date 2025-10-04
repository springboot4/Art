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

import com.art.system.api.user.dto.SystemUserDTO;
import com.art.system.api.user.dto.SystemUserPageDTO;
import com.art.system.api.user.redis.user.UserRedisConstants;
import com.art.system.core.convert.UserConvert;
import com.art.system.dao.dataobject.SystemUserDO;
import com.art.system.dao.mysql.UserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.art.system.api.user.redis.user.UserRedisConstants.USER_DETAILS;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 19:37
 */
@Component
@RequiredArgsConstructor
public class UserManager {

	private final UserMapper userMapper;

	public SystemUserDO getUserById(Long id) {
		return userMapper.getUserById(id);
	}

	@Cacheable(value = UserRedisConstants.USER_INFO, key = "#username", unless = "#result == null")
	public SystemUserDO getUserByName(String username) {
		return userMapper.findByName(username);
	}

	public Page<SystemUserDTO> pageUser(SystemUserPageDTO userPageDTO) {
		return UserConvert.INSTANCE.convert(
				userMapper.findUserDetailPage(Page.of(userPageDTO.getCurrent(), userPageDTO.getSize()), userPageDTO));
	}

	/**
	 * 获取当前租户下的所有用户数
	 * @return 当前租户下的所有用户数
	 */
	public long count() {
		return userMapper.selectCount(Wrappers.emptyWrapper());
	}

	public Long addUser(SystemUserDTO user) {
		SystemUserDO userDO = UserConvert.INSTANCE.convert(user);
		userMapper.insert(userDO);
		return userDO.getUserId();
	}

	@CacheEvict(value = { UserRedisConstants.USER_INFO, USER_DETAILS }, key = "#user.username")
	public void updateUserById(SystemUserDTO user) {
		userMapper.updateById(UserConvert.INSTANCE.convert(user));
	}

	public void deleteUserByIds(List<String> ids) {
		userMapper.deleteBatchIds(ids);
	}

	public SystemUserDO getUserByMobile(String mobile) {
		return userMapper.findByMobile(mobile);
	}

}
