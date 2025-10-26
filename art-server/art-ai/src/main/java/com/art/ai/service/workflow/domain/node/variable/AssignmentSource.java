package com.art.ai.service.workflow.domain.node.variable;

import lombok.Data;

/**
 * 值来源配置
 *
 * @author fxz
 */
@Data
public class AssignmentSource {

	private AssignmentSourceType type;

	/** 常量值 */
	private Object constant;

	/** 引用的变量名（initNodeInputsByReference 生成的 key） */
	private String referenceKey;

}
