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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxz.system.dto.PostDto;
import com.fxz.system.entity.Post;

import java.util.List;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-04-05
 */
public interface PostService extends IService<Post> {

	/**
	 * 添加
	 */
	Boolean addPost(PostDto postDto);

	/**
	 * 修改
	 */
	Boolean updatePost(PostDto postDto);

	/**
	 * 分页
	 */
	IPage<Post> pagePost(Page<Post> pageParam, Post post);

	/**
	 * 获取单条
	 */
	Post findById(Long id);

	/**
	 * 获取全部
	 */
	List<Post> findAll();

	/**
	 * 删除
	 */
	Boolean deletePost(Long id);

}