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

import com.art.system.api.user.dto.UserPostDTO;
import com.art.system.api.user.dto.UserPostPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 用户与岗位关联表
 *
 * @author fxz
 * @date 2022-04-05
 */
public interface UserPostService {

	/**
	 * 添加
	 */
	Boolean addUserPost(UserPostDTO userPostDto);

	/**
	 * 修改
	 */
	Boolean updateUserPost(UserPostDTO userPostDto);

	/**
	 * 分页
	 */
	IPage<UserPostDTO> pageUserPost(UserPostPageDTO pageDTO);

	/**
	 * 获取单条
	 */
	UserPostDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<UserPostDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteUserPost(Long id);

}