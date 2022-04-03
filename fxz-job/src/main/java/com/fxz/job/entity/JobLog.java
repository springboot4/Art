package com.fxz.job.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 定时任务调度日志表
 *
 * @author fxz
 * @date 2022-04-03
 */
@Data
@TableName("sys_job_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class JobLog extends BaseEntity {

	private static final long serialVersionUID = -6956547496195432417L;

	/**
	 * ID
	 */
	private Long jobLogId;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 任务组名
	 */
	private String jobGroup;

	/**
	 * 调用目标字符串
	 */
	private String invokeTarget;

	/**
	 * 日志信息
	 */
	private String jobMessage;

	/**
	 * 执行状态（0正常 1失败）
	 */
	private String status;

	/**
	 * 异常信息
	 */
	private String exceptionInfo;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 停止时间
	 */
	private Date stopTime;

}
