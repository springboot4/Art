package com.art.ai.core.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/12 12:40
 */
@Data
public class WorkflowRunDTO {

	private Map<String, Object> inputs = new HashMap<>();

	private Map<String, Object> systems = new HashMap<>();

	private String workflowId;

}
