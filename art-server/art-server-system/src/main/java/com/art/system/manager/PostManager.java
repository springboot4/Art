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

import com.art.system.api.post.dto.PostDTO;
import com.art.system.api.post.dto.PostPageDTO;
import com.art.system.core.convert.PostConvert;
import com.art.system.dao.dataobject.PostDO;
import com.art.system.dao.mysql.PostMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 21:42
 */
@Component
@RequiredArgsConstructor
public class PostManager {

	private final PostMapper postMapper;

	public Integer addPost(PostDTO postDto) {
		return postMapper.insert(PostConvert.INSTANCE.convert(postDto));
	}

	public Integer updatePostById(PostDTO postDto) {
		return postMapper.updateById(PostConvert.INSTANCE.convert(postDto));
	}

	public Integer deletePostById(Long id) {
		return postMapper.deleteById(id);
	}

	public PostDO getPostById(Long id) {
		return postMapper.selectById(id);
	}

	public List<PostDO> listPost() {
		return postMapper.selectList(Wrappers.emptyWrapper());
	}

	public Page<PostDO> pagePost(PostPageDTO postPageDTO) {
		LambdaQueryWrapper<PostDO> wrapper = Wrappers.<PostDO>lambdaQuery()
			.like(StringUtils.isNotBlank(postPageDTO.getPostCode()), PostDO::getPostCode, postPageDTO.getPostCode())
			.like(StringUtils.isNotBlank(postPageDTO.getPostName()), PostDO::getPostName, postPageDTO.getPostName());
		return postMapper.selectPage(Page.of(postPageDTO.getCurrent(), postPageDTO.getSize()), wrapper);
	}

}
