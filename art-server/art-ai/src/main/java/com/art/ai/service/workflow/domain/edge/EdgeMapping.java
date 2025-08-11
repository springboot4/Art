package com.art.ai.service.workflow.domain.edge;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 21:52
 */
public record EdgeMapping(String sourceNodeId, Map<String, String> mapping) {
	public EdgeMapping(String sourceNodeId) {
		this(sourceNodeId, new HashMap<>());
	}
}
