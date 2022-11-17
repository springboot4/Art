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

package com.art.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.art.system.dto.UserPostDto;
import com.art.system.entity.UserPost;
import com.art.system.mapper.UserPostMapper;
import com.art.system.service.UserPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
public class UserPostServiceImpl extends ServiceImpl<UserPostMapper, UserPost> implements UserPostService {

	private final UserPostMapper userPostMapper;

	/**
	 * 添加
	 */
	@Override
	public Boolean addUserPost(UserPostDto userPostDto) {
		UserPost userPost = new UserPost();
		BeanUtils.copyProperties(userPostDto, userPost);
		userPostMapper.insert(userPost);
		return Boolean.TRUE;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateUserPost(UserPostDto userPostDto) {
		UserPost userPost = new UserPost();
		BeanUtils.copyProperties(userPostDto, userPost);
		userPostMapper.updateById(userPost);
		return Boolean.TRUE;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<UserPost> pageUserPost(Page<UserPost> pageParam, UserPost userPost) {
		return userPostMapper.selectPage(pageParam, Wrappers.emptyWrapper());
	}

	/**
	 * 获取单条
	 */
	@Override
	public UserPost findById(Long id) {
		return userPostMapper.selectById(id);
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<UserPost> findAll() {
		return userPostMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteUserPost(Long id) {
		userPostMapper.deleteById(id);
		return Boolean.TRUE;
	}

}