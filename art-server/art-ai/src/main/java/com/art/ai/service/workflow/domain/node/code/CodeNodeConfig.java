package com.art.ai.service.workflow.domain.node.code;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.Data;

/**
 * @author fxz
 * @since 2025/8/10 16:37
 */
@Data
public class CodeNodeConfig extends NodeConfig {

	private String code;

	private String language;

}
