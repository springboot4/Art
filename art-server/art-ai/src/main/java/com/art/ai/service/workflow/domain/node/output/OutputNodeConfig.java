package com.art.ai.service.workflow.domain.node.output;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import lombok.Data;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 16:42
 */
@Data
public class OutputNodeConfig extends NodeConfig {

	private List<NodeOutputVariable> outputVariables;

}
