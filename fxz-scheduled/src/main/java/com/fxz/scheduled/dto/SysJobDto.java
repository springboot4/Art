package com.fxz.scheduled.dto;

import com.fxz.scheduled.entity.SysJob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 定时任务调度表
 *
 * @author fxz
 * @date 2022-04-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysJobDto extends SysJob {

	private static final long serialVersionUID = -1L;

}