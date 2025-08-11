package com.art.ai.service.workflow.domain.node.knowledge;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.Data;

/**
 * @author fxz
 * @since 2025/8/10 16:50
 */
@Data
public class KnowledgeNodeConfig extends NodeConfig {

	private int topK;

	private String query;

	private double threshold;

}
