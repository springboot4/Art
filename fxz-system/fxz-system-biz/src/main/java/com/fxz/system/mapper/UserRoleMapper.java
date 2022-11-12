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

package com.fxz.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fxz.system.entity.UserRole;
import org.apache.ibatis.annotations.Param;

/**
 * @author fxz
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

	/**
	 * 根据用户Id删除该用户的角色关系
	 * @param userIds 用户 ID
	 * @return boolean
	 */
	Boolean deleteByUserId(@Param("userIds") String[] userIds);

	/**
	 * 根据角色Id删除该角色的用户关系
	 * @param roleIds 角色 ID
	 * @return boolean
	 */
	Boolean deleteByRoleId(@Param("roleIds") String[] roleIds);

}