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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.art.common.mp.result.Result;
import com.art.system.dto.DictDto;
import com.art.system.entity.Dict;
import com.art.system.entity.DictItem;

import java.util.List;

/**
 * 字典表
 *
 * @author fxz
 * @date 2022-04-04
 */
public interface DictService extends IService<Dict> {

	/**
	 * 添加
	 */
	Boolean addDict(DictDto dictDto);

	/**
	 * 修改
	 */
	Result<Void> updateDict(DictDto dictDto);

	/**
	 * 分页
	 */
	IPage<Dict> pageDict(Page<Dict> pageParam, Dict dict);

	/**
	 * 获取单条
	 */
	Dict findById(Long id);

	/**
	 * 获取全部
	 */
	List<Dict> findAll();

	/**
	 * 删除
	 */
	Result<Void> deleteDict(Long id);

	/**
	 * 根据字典类型获取字典下的所有字典项
	 * @param type 字典类型
	 * @return 字典项
	 */
	List<DictItem> getDictItemsByType(String type);

}