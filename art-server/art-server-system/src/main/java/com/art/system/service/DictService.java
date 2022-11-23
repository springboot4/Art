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

import com.art.system.api.dict.dto.DictDTO;
import com.art.system.api.dict.dto.DictItemDTO;
import com.art.system.api.dict.dto.DictPageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 字典表
 *
 * @author fxz
 * @date 2022-04-04
 */
public interface DictService {

    /**
     * 添加
     */
    Boolean addDict(DictDTO dictDto);

    /**
     * 修改
     */
    Boolean updateDict(DictDTO dictDto);

    /**
     * 分页
     */
    IPage<DictDTO> pageDict(DictPageDTO dictPageDTO);

    /**
     * 获取单条
     */
    DictDTO findById(Long id);

    /**
     * 获取全部
     */
    List<DictDTO> findAll();

    /**
     * 删除
     */
    Boolean deleteDict(Long id);

    /**
     * 根据字典类型获取字典下的所有字典项
     *
     * @param type 字典类型
     * @return 字典项
     */
    List<DictItemDTO> getDictItemsByType(String type);

}