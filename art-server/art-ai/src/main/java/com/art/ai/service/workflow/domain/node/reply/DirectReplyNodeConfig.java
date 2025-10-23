package com.art.ai.service.workflow.domain.node.reply;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.Data;

/**
 * @author fxz
 * @since 2025/10/23 13:16
 */
@Data
public class DirectReplyNodeConfig extends NodeConfig {

	private String replyText;

}
