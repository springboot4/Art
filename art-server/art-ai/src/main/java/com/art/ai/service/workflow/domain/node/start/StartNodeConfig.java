package com.art.ai.service.workflow.domain.node.start;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import com.art.ai.service.workflow.domain.node.NodeInputVariable;
import lombok.Data;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 15:07
 */
@Data
public class StartNodeConfig extends NodeConfig {

	private List<NodeInputVariable> userInputs;

}
