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

import com.art.system.dao.dataobject.PostDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.art.system.api.post.PostDTO;
import com.art.system.dao.dataobject.UserPostDO;
import com.art.system.dao.mysql.PostMapper;
import com.art.system.service.PostService;
import com.art.system.service.UserPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-04-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, PostDO> implements PostService {

	private final PostMapper postMapper;

	private final UserPostService userPostService;

	/**
	 * 添加
	 */
	@Override
	public Boolean addPost(PostDTO postDto) {
		PostDO postDO = new PostDO();
		BeanUtils.copyProperties(postDto, postDO);
		postMapper.insert(postDO);
		return Boolean.TRUE;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updatePost(PostDTO postDto) {
		PostDO postDO = new PostDO();
		BeanUtils.copyProperties(postDto, postDO);
		postMapper.updateById(postDO);
		return Boolean.TRUE;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<PostDO> pagePost(Page<PostDO> pageParam, PostDO postDO) {
		return postMapper.selectPage(pageParam,
				Wrappers.<PostDO>lambdaQuery()
						.like(StringUtils.isNotBlank(postDO.getPostCode()), PostDO::getPostCode, postDO.getPostCode())
						.like(StringUtils.isNotBlank(postDO.getPostName()), PostDO::getPostName, postDO.getPostName()));
	}

	/**
	 * 获取单条
	 */
	@Override
	public PostDO findById(Long id) {
		return postMapper.selectById(id);
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<PostDO> findAll() {
		return postMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 删除
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean deletePost(Long id) {
		userPostService.remove(Wrappers.<UserPostDO>lambdaQuery().eq(UserPostDO::getPostId, id));
		postMapper.deleteById(id);
		return Boolean.TRUE;
	}

}