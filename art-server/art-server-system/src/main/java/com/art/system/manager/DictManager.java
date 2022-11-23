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

import com.art.system.api.dict.dto.DictDTO;
import com.art.system.api.dict.dto.DictPageDTO;
import com.art.system.core.convert.DictConvert;
import com.art.system.dao.dataobject.DictDO;
import com.art.system.dao.mysql.DictMapper;
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
 * @date 2022/11/23 13:26
 */
@Component
@RequiredArgsConstructor
public class DictManager {

    private final DictMapper dictMapper;

    public Integer addDict(DictDTO dictDto) {
        return dictMapper.insert(DictConvert.INSTANCE.convert(dictDto));
    }

    public Integer updateById(DictDTO dictDto) {
        return dictMapper.updateById(DictConvert.INSTANCE.convert(dictDto));
    }

    public DictDO getDictById(Long id) {
        return dictMapper.selectById(id);
    }

    public Integer deleteDictById(Long id) {
        return dictMapper.deleteById(id);
    }

    public List<DictDO> listDict() {
        return dictMapper.selectList(Wrappers.emptyWrapper());
    }

    public Page<DictDO> pageDict(DictPageDTO dictPageDTO) {
        LambdaQueryWrapper<DictDO> wrapper = Wrappers.<DictDO>lambdaQuery().
                like(StringUtils.isNotBlank(dictPageDTO.getType()), DictDO::getType, dictPageDTO.getType())
                .eq(StringUtils.isNotBlank(dictPageDTO.getSystemFlag()), DictDO::getSystemFlag, dictPageDTO.getSystemFlag());

        return dictMapper.selectPage(Page.of(dictPageDTO.getCurrent(), dictPageDTO.getSize()), wrapper);
    }

}
