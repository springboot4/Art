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

import com.art.common.core.enums.DictTypeEnum;
import com.art.common.core.exception.FxzException;
import com.art.system.api.dict.dto.DictDTO;
import com.art.system.api.dict.dto.DictItemDTO;
import com.art.system.api.dict.dto.DictPageDTO;
import com.art.system.core.convert.DictConvert;
import com.art.system.core.convert.DictItemConvert;
import com.art.system.dao.dataobject.DictDO;
import com.art.system.manager.DictItemManager;
import com.art.system.manager.DictManager;
import com.art.system.service.DictService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典表
 *
 * @author fxz
 * @date 2022-04-04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

	private final DictManager dictManager;

	private final DictItemManager dictItemManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addDict(DictDTO dictDto) {
		return dictManager.addDict(dictDto) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateDict(DictDTO dictDto) {
		if (DictTypeEnum.SYSTEM.getType().equals(dictDto.getSystemFlag())) {
			throw new FxzException("系统内置字典，不可修改!");
		}

		return dictManager.updateById(dictDto) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<DictDTO> pageDict(DictPageDTO dictPageDTO) {
		return DictConvert.INSTANCE.convert(dictManager.pageDict(dictPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public DictDTO findById(Long id) {
		return DictConvert.INSTANCE.convert(dictManager.getDictById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<DictDTO> findAll() {
		return DictConvert.INSTANCE.convert(dictManager.listDict());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteDict(Long id) {
		DictDO dictDO = dictManager.getDictById(id);
		if (DictTypeEnum.SYSTEM.getType().equals(dictDO.getSystemFlag())) {
			throw new FxzException("系统内置字典，不可删除!");
		}

		// 删除所有字典项
		dictItemManager.deleteDictItemByDictId(id);
		// 删除字典
		return dictManager.deleteDictById(id) > 0;
	}

	/**
	 * 根据字典类型获取字典下的所有字典项
	 * @param type 字典类型
	 * @return 字典项
	 */
	@Override
	public List<DictItemDTO> getDictItemsByType(String type) {
		return DictItemConvert.INSTANCE.convertList(dictItemManager.getDictItemsByType(type));
	}

}