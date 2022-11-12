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

package com.fxz.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxz.common.log.enums.BusinessType;
import com.fxz.common.mp.base.BaseCreateEntity;
import com.fxz.system.dto.OperLogDto;
import com.fxz.system.entity.OperLog;
import com.fxz.system.mapper.OperLogMapper;
import com.fxz.system.service.OperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 操作日志记录
 *
 * @author fxz
 * @date 2022-04-07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

	private final OperLogMapper operLogMapper;

	/**
	 * 添加
	 */
	@Override
	public Boolean addOperLog(OperLogDto operLogDto) {
		log.info("保存日志");
		OperLog operLog = new OperLog();
		BeanUtils.copyProperties(operLogDto, operLog);
		operLogMapper.insert(operLog);
		return Boolean.TRUE;
	}

	/**
	 * 修改
	 */
	@Override
	public Boolean updateOperLog(OperLogDto operLogDto) {
		OperLog operLog = new OperLog();
		BeanUtils.copyProperties(operLogDto, operLog);
		operLogMapper.updateById(operLog);
		return Boolean.TRUE;
	}

	/**
	 * 分页
	 */
	@Override
	public IPage<OperLog> pageOperLog(Page<OperLog> pageParam, OperLog operLog) {
		QueryWrapper<OperLog> queryWrapper = Wrappers.query();
		if (Objects.isNull(operLog.getBusinessType())) {
			queryWrapper.ne(StringUtils.camelToUnderline(OperLog.Fields.businessType), BusinessType.GRANT);
		}
		else {
			queryWrapper.eq(StringUtils.camelToUnderline(OperLog.Fields.businessType), operLog.getBusinessType());
		}
		queryWrapper.like(StringUtils.isNotBlank(operLog.getTitle()), OperLog.Fields.title, operLog.getTitle())
				.orderByDesc(StringUtils.camelToUnderline(BaseCreateEntity.Fields.createTime));

		return operLogMapper.selectPage(pageParam, queryWrapper);
	}

	/**
	 * 获取单条
	 */
	@Override
	public OperLog findById(Long id) {
		return operLogMapper.selectById(id);
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<OperLog> findAll() {
		return operLogMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 删除
	 */
	@Override
	public Boolean deleteOperLog(Long id) {
		operLogMapper.deleteById(id);
		return Boolean.TRUE;
	}

}