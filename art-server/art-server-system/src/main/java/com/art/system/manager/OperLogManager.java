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

import com.art.common.log.core.enums.BusinessType;
import com.art.common.mp.core.base.BaseCreateEntity;
import com.art.system.api.log.dto.OperLogDTO;
import com.art.system.api.log.dto.OperLogPageDTO;
import com.art.system.core.convert.OperLogConvert;
import com.art.system.dao.dataobject.OperLogDO;
import com.art.system.dao.mysql.OperLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 20:43
 */
@Component
@RequiredArgsConstructor
public class OperLogManager {

	private final OperLogMapper operLogMapper;

	public Integer addOperLog(OperLogDTO operLogDto) {
		return operLogMapper.insert(OperLogConvert.INSTANCE.convert(operLogDto));
	}

	public Integer updateOperLogById(OperLogDTO operLogDto) {
		return operLogMapper.updateById(OperLogConvert.INSTANCE.convert(operLogDto));
	}

	public Integer deleteOperLogById(Long id) {
		return operLogMapper.deleteById(id);
	}

	public OperLogDO selectOperLogById(Long id) {
		return operLogMapper.selectById(id);
	}

	public List<OperLogDO> listOperLog() {
		return operLogMapper.selectList(Wrappers.emptyWrapper());
	}

	public Page<OperLogDO> pageOperLog(OperLogPageDTO operLogPageDTO) {
		QueryWrapper<OperLogDO> queryWrapper = Wrappers.query();
		if (Objects.isNull(operLogPageDTO.getBusinessType())) {
			queryWrapper.ne(StringUtils.camelToUnderline(OperLogDO.Fields.businessType), BusinessType.GRANT);
		}
		else {
			queryWrapper.eq(StringUtils.camelToUnderline(OperLogDO.Fields.businessType),
					operLogPageDTO.getBusinessType());
		}
		queryWrapper
			.like(StringUtils.isNotBlank(operLogPageDTO.getTitle()), OperLogDO.Fields.title, operLogPageDTO.getTitle())
			.orderByDesc(StringUtils.camelToUnderline(BaseCreateEntity.Fields.createTime));

		return operLogMapper.selectPage(Page.of(operLogPageDTO.getCurrent(), operLogPageDTO.getSize()), queryWrapper);
	}

}
