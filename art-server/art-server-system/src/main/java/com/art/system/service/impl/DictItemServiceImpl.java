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

import com.art.core.common.constant.DictTypeEnum;
import com.art.core.common.exception.ArtException;
import com.art.system.api.dict.dto.DictItemDTO;
import com.art.system.api.dict.dto.DictItemExistsDTO;
import com.art.system.api.dict.dto.DictItemPageDTO;
import com.art.system.core.convert.DictItemConvert;
import com.art.system.dao.dataobject.DictDO;
import com.art.system.dao.dataobject.DictItemDO;
import com.art.system.manager.DictItemManager;
import com.art.system.manager.DictManager;
import com.art.system.service.DictItemService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典项
 *
 * @author fxz
 * @date 2022-04-04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictItemServiceImpl implements DictItemService {

	private final DictItemManager dictItemManager;

	private final DictManager dictManager;

	/**
	 * 添加
	 */
	@Override
	public Boolean addDictItem(DictItemDTO dictItemDto) {
		return dictItemManager.addDictItem(DictItemConvert.INSTANCE.convert(dictItemDto)) > 0;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateDictItem(DictItemDTO dictItemDto) {
		DictDO dictDO = dictManager.getDictById(dictItemDto.getDictId());
		if (DictTypeEnum.SYSTEM.getType().equals(dictDO.getSystemFlag())) {
			throw new ArtException("系统内置字典，不可修改!");
		}

		return dictItemManager.updateDictItemById(DictItemConvert.INSTANCE.convert(dictItemDto)) > 0;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<DictItemDTO> pageDictItem(DictItemPageDTO dictItemPageDTO) {
		return DictItemConvert.INSTANCE.convert(dictItemManager.pageDictItem(dictItemPageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public DictItemDTO findById(Long id) {
		return DictItemConvert.INSTANCE.convert(dictItemManager.getDictItemById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<DictItemDTO> findAll() {
		return DictItemConvert.INSTANCE.convertList(dictItemManager.listDictItem());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteDictItem(Long id) {
		DictItemDO item = dictItemManager.getDictItemById(id);
		DictDO dictDO = dictManager.getDictById(item.getDictId());
		if (DictTypeEnum.SYSTEM.getType().equals(dictDO.getSystemFlag())) {
			throw new ArtException("系统内置字典，不可删除!");
		}

		return dictItemManager.deleteDictItemByItemId(id) > 0;
	}

	/**
	 * 校验字典项编码是否已经被使用 新增且编码被使用、修改且编码被别的字典项使用 true 其他条件下返回false
	 * @return true or false
	 */
	@Override
	public Boolean itemExistsByCode(DictItemExistsDTO dictItemExistsDTO) {
		// 查询此字典下 该编码是否已经使用
		Boolean existsDictItem = dictItemManager.existsDictItem(dictItemExistsDTO);

		// 如果已经被使用 判断此次是否允许操作
		if (existsDictItem) {
			if (dictItemExistsDTO.getId() == null) {
				// 如果是新增 则不可以新增
				return Boolean.TRUE;
			}
			else {
				DictItemDO dictItemDO = dictItemManager.getDictItemById(dictItemExistsDTO.getId());
				// 如果是修改 判断id是否和使用此编码的字典项相同
				return !(dictItemExistsDTO.getId().equals(dictItemDO.getId()));
			}
		}

		return Boolean.FALSE;
	}

}