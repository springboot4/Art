package com.art.ai.service.agent.tool;

import lombok.Builder;
import lombok.Data;

/**
 * 模型需填写的工具参数描述
 *
 * @author fxz
 */
@Data
@Builder
public class ToolArgumentDescriptor {

	private String name;

	private String type;

	private boolean required;

	private String description;

}
