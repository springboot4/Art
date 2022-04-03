package com.fxz.job.dto;

import com.fxz.job.entity.JobLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 定时任务调度日志表
 *
 * @author fxz
 * @date 2022-04-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JobLogDto extends JobLog {

	private static final long serialVersionUID = -2784035579026358236L;

}