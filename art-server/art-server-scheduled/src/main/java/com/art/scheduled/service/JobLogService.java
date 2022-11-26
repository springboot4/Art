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

package com.art.scheduled.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.art.common.core.result.PageResult;
import com.art.scheduled.core.entity.JobLog;
import com.art.scheduled.dao.mysql.JobLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务调度日志表
 *
 * @author fxz
 * @date 2022-04-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobLogService {

	private final JobLogMapper jobLogMapper;

	/**
	 * 添加
	 */
	public JobLog add(JobLog param) {
		jobLogMapper.insert(param);
		return param;
	}

	/**
	 * 修改
	 */
	public JobLog update(JobLog param) {
		jobLogMapper.updateById(param);
		return param;
	}

	/**
	 * 分页
	 */
	public PageResult<JobLog> page(Page<JobLog> page, JobLog jobLog) {
		return PageResult.success(jobLogMapper.selectPage(page,
				Wrappers.<JobLog>lambdaQuery()
						.eq(StringUtils.isNotBlank(jobLog.getJobName()), JobLog::getJobName, jobLog.getJobName())
						.eq(StringUtils.isNotBlank(jobLog.getJobGroup()), JobLog::getJobGroup, jobLog.getJobGroup())));
	}

	/**
	 * 获取单条
	 */
	public JobLog findById(Long id) {
		return jobLogMapper.selectById(id);
	}

	/**
	 * 获取全部
	 */
	public List<JobLog> findAll() {
		return jobLogMapper.selectList(Wrappers.emptyWrapper());
	}

	/**
	 * 保存job执行日志
	 * @param jobLog job执行信息
	 */
	public void addJobLog(JobLog jobLog) {
		jobLogMapper.insert(jobLog);
	}

}