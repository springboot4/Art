package com.art.ai.service.workflow.variable;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fxz
 * @since 2025/8/16 00:22
 */
@UtilityClass
public class VariableRenderUtils {

	/**
	 * 渲染变量
	 */
	public String render(String text) {
		if (StringUtils.isBlank(text)) {
			return "";
		}
		return text.replaceAll("\\$\\{(.+?)}", "$1").replace(".", "_");
	}

}
