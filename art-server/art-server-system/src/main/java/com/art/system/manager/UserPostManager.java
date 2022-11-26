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

package com.art.system.manager;

import com.art.system.api.user.dto.UserPostPageDTO;
import com.art.system.dao.dataobject.UserPostDO;
import com.art.system.dao.mysql.UserPostMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/26 17:05
 */
@RequiredArgsConstructor
@Component
public class UserPostManager {

	private final UserPostMapper userPostMapper;

	public void addUserPosts(List<UserPostDO> list) {
		list.forEach(userPostMapper::insert);
	}

	public void deleteUserPostByUserId(Long userId) {
		userPostMapper.delete(Wrappers.<UserPostDO>lambdaQuery().eq(UserPostDO::getUserId, userId));
	}

	public Integer updateUserPostById(UserPostDO userPostDO) {
		return userPostMapper.updateById(userPostDO);
	}

	public Integer deleteUserPostByrId(Long id) {
		return userPostMapper.deleteById(id);
	}

	public UserPostDO getUserPostById(Long id) {
		return userPostMapper.selectById(id);
	}

	public List<UserPostDO> listUserPost() {
		return userPostMapper.selectList(Wrappers.emptyWrapper());
	}

	public Page<UserPostDO> pageUserPost(UserPostPageDTO pageDTO) {
		return userPostMapper.selectPage(Page.of(pageDTO.getCurrent(), pageDTO.getSize()), Wrappers.emptyWrapper());
	}

}
