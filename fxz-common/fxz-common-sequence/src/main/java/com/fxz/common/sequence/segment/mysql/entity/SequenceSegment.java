package com.fxz.common.sequence.segment.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;

/**
 * 队列号段
 *
 * @author fxz
 */
@Data
@TableName("sys_sequence_segment")
public class SequenceSegment extends BaseCreateEntity {

	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 号段key
	 */
	private String segmentKey;

	/**
	 * 号段开始值
	 */
	private Long segmentValue;

}