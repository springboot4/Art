package com.art.ai.service.workflow.dsl;

import com.art.ai.service.workflow.domain.edge.EdgeValue;
import com.art.ai.service.workflow.domain.node.WorkflowNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 20:23
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class GraphDSL {

	private String name;

	private List<WorkflowNode<?>> nodes;

	private List<EdgeValue> edges;

}
