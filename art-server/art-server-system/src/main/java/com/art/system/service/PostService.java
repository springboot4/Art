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

package com.art.system.service;

import com.art.system.dao.dataobject.PostDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.art.system.api.post.PostDTO;

import java.util.List;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-04-05
 */
public interface PostService extends IService<PostDO> {

	/**
	 * 添加
	 */
	Boolean addPost(PostDTO postDto);

	/**
	 * 修改
	 */
	Boolean updatePost(PostDTO postDto);

	/**
	 * 分页
	 */
	IPage<PostDO> pagePost(Page<PostDO> pageParam, PostDO postDO);

	/**
	 * 获取单条
	 */
	PostDO findById(Long id);

	/**
	 * 获取全部
	 */
	List<PostDO> findAll();

	/**
	 * 删除
	 */
	Boolean deletePost(Long id);

}