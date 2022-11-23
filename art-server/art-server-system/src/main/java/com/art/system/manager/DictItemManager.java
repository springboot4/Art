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

import com.art.system.api.dict.dto.DictItemExistsDTO;
import com.art.system.api.dict.dto.DictItemPageDTO;
import com.art.system.dao.dataobject.DictItemDO;
import com.art.system.dao.mysql.DictItemMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 13:54
 */
@Component
@RequiredArgsConstructor
public class DictItemManager {

    private final DictItemMapper dictItemMapper;

    public void deleteDictItemByDictId(Long id) {
        dictItemMapper.delete(Wrappers.<DictItemDO>lambdaQuery().eq(DictItemDO::getDictId, id));
    }

    public List<DictItemDO> getDictItemsByType(String type) {
        return dictItemMapper.selectList(Wrappers.<DictItemDO>lambdaQuery().eq(DictItemDO::getType, type).orderByAsc(DictItemDO::getSortOrder));
    }

    public Integer addDictItem(DictItemDO dictItemDO) {
        return dictItemMapper.insert(dictItemDO);
    }

    public Integer updateDictItemById(DictItemDO dictItemDO) {
        return dictItemMapper.updateById(dictItemDO);
    }

    public DictItemDO getDictItemById(Long id) {
        return dictItemMapper.selectById(id);
    }

    public Integer deleteDictItemByItemId(Long id) {
        return dictItemMapper.deleteById(id);
    }

    public List<DictItemDO> listDictItem() {
        return dictItemMapper.selectList(Wrappers.emptyWrapper());
    }

    public Page<DictItemDO> pageDictItem(DictItemPageDTO dictItemPageDTO) {
        LambdaQueryWrapper<DictItemDO> wrapper = Wrappers.<DictItemDO>lambdaQuery()
                .eq(dictItemPageDTO.getDictId() != null, DictItemDO::getDictId, dictItemPageDTO.getDictId())
                .orderByAsc(DictItemDO::getSortOrder);

        return dictItemMapper.selectPage(Page.of(dictItemPageDTO.getCurrent(), dictItemPageDTO.getSize()), wrapper);
    }

    public Boolean existsDictItem(DictItemExistsDTO dictItemExistsDTO) {
        DictItemDO dictItemDO = dictItemMapper.selectOne(Wrappers.<DictItemDO>lambdaQuery()
                .eq(DictItemDO::getDictId, dictItemExistsDTO.getDictId())
                .eq(DictItemDO::getValue, dictItemExistsDTO.getValue())
                .last("limit 1"));

        return Objects.nonNull(dictItemDO);
    }

}
