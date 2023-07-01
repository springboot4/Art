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

import com.art.system.api.user.dto.UserPostDTO;
import com.art.system.api.user.dto.UserPostPageDTO;
import com.art.system.core.convert.UserPostConvert;
import com.art.system.dao.dataobject.UserPostDO;
import com.art.system.manager.UserPostManager;
import com.art.system.service.UserPostService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 用户与岗位关联表
 *
 * @author fxz
 * @date 2022-04-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPostServiceImpl implements UserPostService {

	private final UserPostManager userPostManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addUserPost(UserPostDTO userPostDto) {
		UserPostDO postDO = UserPostConvert.INSTANCE.convert(userPostDto);
		userPostManager.addUserPosts(Collections.singletonList(postDO));
		return Boolean.TRUE;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateUserPost(UserPostDTO userPostDto) {
		return userPostManager.updateUserPostById(UserPostConvert.INSTANCE.convert(userPostDto)) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<UserPostDTO> pageUserPost(UserPostPageDTO pageDTO) {
		return UserPostConvert.INSTANCE.convert(userPostManager.pageUserPost(pageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public UserPostDTO findById(Long id) {
		return UserPostConvert.INSTANCE.convert(userPostManager.getUserPostById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<UserPostDTO> findAll() {
		return UserPostConvert.INSTANCE.convert(userPostManager.listUserPost());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteUserPost(Long id) {
		return userPostManager.deleteUserPostByrId(id) > 0;
	}

}