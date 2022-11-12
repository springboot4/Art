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
import com.fxz.common.mp.result.Result;
import com.fxz.system.dto.DictItemDto;
import com.fxz.system.entity.DictItem;

import java.util.List;

/**
 * 字典项
 *
 * @author fxz
 * @date 2022-04-04
 */
public interface DictItemService extends IService<DictItem> {

	/**
	 * 添加
	 */
	Boolean addDictItem(DictItemDto dictItemDto);

	/**
	 * 修改
	 */
	Result<Void> updateDictItem(DictItemDto dictItemDto);

	/**
	 * 分页
	 */
	IPage<DictItem> pageDictItem(Page<DictItem> pageParam, DictItem dictItem);

	/**
	 * 获取单条
	 */
	DictItem findById(Long id);

	/**
	 * 获取全部
	 */
	List<DictItem> findAll();

	/**
	 * 删除
	 */
	Result<Void> deleteDictItem(Long id);

	/**
	 * 校验字典项编码是否已经被使用
	 * @param id 字典项id
	 * @param dictId 字典id
	 * @param value 字典项编码
	 * @return true or false
	 */
	Boolean itemExistsByCode(Long id, Long dictId, String value);

}