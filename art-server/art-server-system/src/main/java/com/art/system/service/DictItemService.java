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

import com.art.system.api.dict.dto.DictItemDTO;
import com.art.system.api.dict.dto.DictItemExistsDTO;
import com.art.system.api.dict.dto.DictItemPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 字典项
 *
 * @author fxz
 * @date 2022-04-04
 */
public interface DictItemService {

	/**
	 * 添加
	 */
	Boolean addDictItem(DictItemDTO dictItemDto);

	/**
	 * 修改
	 */
	Boolean updateDictItem(DictItemDTO dictItemDto);

	/**
	 * 分页
	 */
	IPage<DictItemDTO> pageDictItem(DictItemPageDTO dictItemPageDTO);

	/**
	 * 获取单条
	 */
	DictItemDTO findById(Long id);

	/**
	 * 获取全部
	 */
	List<DictItemDTO> findAll();

	/**
	 * 删除
	 */
	Boolean deleteDictItem(Long id);

	/**
	 * 校验字典项编码是否已经被使用
	 * @return true or false
	 */
	Boolean itemExistsByCode(DictItemExistsDTO dictItemExistsDTO);

}