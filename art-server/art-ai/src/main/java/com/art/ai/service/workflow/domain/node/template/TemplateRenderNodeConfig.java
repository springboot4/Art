package com.art.ai.service.workflow.domain.node.template;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.Data;

/**
 * @author fxz
 * @since 2025/8/10 15:22
 */
@Data
public class TemplateRenderNodeConfig extends NodeConfig {

	private String template;

}
