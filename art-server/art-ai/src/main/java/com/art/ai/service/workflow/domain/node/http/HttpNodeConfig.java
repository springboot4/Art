package com.art.ai.service.workflow.domain.node.http;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.Data;

/**
 * @author fxz
 * @since 2025/8/10 16:38
 */
@Data
public class HttpNodeConfig extends NodeConfig {

	private String method;

	private String url;

	private String headers;

	private String body;

}
