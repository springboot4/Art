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

import com.art.system.api.post.dto.PostDTO;
import com.art.system.api.post.dto.PostPageDTO;
import com.art.system.core.convert.PostConvert;
import com.art.system.manager.PostManager;
import com.art.system.service.PostService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class PostServiceImpl implements PostService {

	private final PostManager postManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addPost(PostDTO postDto) {
		return postManager.addPost(postDto) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updatePost(PostDTO postDto) {
		return postManager.updatePostById(postDto) > 0;
	}

	/**
	 * 获取单条
	 */
	@Override
	public PostDTO findById(Long id) {
		return PostConvert.INSTANCE.convert(postManager.getPostById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<PostDTO> findAll() {
		return PostConvert.INSTANCE.convert(postManager.listPost());
	}

	/**
	 * 删除
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean deletePost(Long id) {
		return postManager.deletePostById(id) > 0;
	}

	/**
	 * 分页
	 * @param postPageDTO 分页参数
	 * @return 分页数据
	 */
	@Override
	public Page<PostDTO> pagePost(PostPageDTO postPageDTO) {
		return PostConvert.INSTANCE.convert(postManager.pagePost(postPageDTO));
	}

}