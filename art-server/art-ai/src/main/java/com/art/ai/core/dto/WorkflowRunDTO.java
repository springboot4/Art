package com.art.ai.core.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author fxz
 * @since 2025/8/12 12:40
 */
@Data
public class WorkflowRunDTO {

	private Map<String, Object> inputs = new HashMap<>();

	private String conversationId = UUID.randomUUID().toString();

	private String workflowId;

}
