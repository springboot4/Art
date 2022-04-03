package com.fxz.job.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fxz.common.core.param.PageParam;
import com.fxz.common.mp.result.PageResult;
import com.fxz.job.dto.JobLogDto;
import com.fxz.job.entity.JobLog;
import com.fxz.job.mapper.JobLogMapper;
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
	public PageResult<JobLogDto> page(PageParam pageParam, JobLog jobLog) {
		return null;
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

	public void addJobLog(JobLog jobLog) {
	}

}