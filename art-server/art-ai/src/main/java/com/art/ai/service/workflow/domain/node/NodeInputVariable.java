package com.art.ai.service.workflow.domain.node;

/**
 * @author fxz
 * @since 2025/8/10 15:09
 */
public record NodeInputVariable(String name, String dataType, boolean required, String description,
		String displayName) {
}
