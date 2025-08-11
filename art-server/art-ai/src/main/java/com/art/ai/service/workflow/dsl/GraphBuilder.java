package com.art.ai.service.workflow.dsl;

import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.edge.EdgeValue;
import com.art.ai.service.workflow.domain.node.WorkflowNode;
import com.art.ai.service.workflow.domain.node.start.StartNodeData;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * todo fxz 支持中断、支持持久化、支持人机交互
 *
 * @author fxz
 */
@UtilityClass
public class GraphBuilder {

	public static StateGraph<NodeState> buildGraph(GraphDSL dsl, WorkFlowContext workFlowContext) {
		List<WorkflowNode<?>> nodes = dsl.getNodes();
		List<EdgeValue> edges = dsl.getEdges();

		var stateGraph = new StateGraph<>(new ObjectStreamStateSerializer<>(NodeState::new));

		WorkflowNode<?> startNode = nodes.stream()
			.filter(n -> n.getData() instanceof StartNodeData)
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("图结构中未找到开始节点 (StartNodeData)"));
		addEdgeToGraph(stateGraph, START, startNode.getId());

		nodes.forEach(node -> {
			try {
				addNodeToGraph(stateGraph, node, workFlowContext);
			}
			catch (GraphStateException e) {
				throw new RuntimeException(e);
			}
		});

		Map<Boolean, List<EdgeValue>> partitionedEdges = edges.stream()
			.collect(Collectors.partitioningBy(e -> StringUtils.equals("source_handle", e.sourceHandle())));

		partitionedEdges.get(true).forEach(edge -> addEdgeToGraph(stateGraph, edge.source(), edge.target()));

		partitionedEdges.get(false)
			.stream()
			.collect(Collectors.groupingBy(EdgeValue::source,
					Collectors.toMap(EdgeValue::sourceHandle, EdgeValue::target)))
			.forEach((sourceNode, mapping) -> addConditionalEdgesToGraph(stateGraph, sourceNode, mapping));

		Set<String> sourceNodeIds = edges.stream().map(EdgeValue::source).collect(Collectors.toSet());

		nodes.stream()
			.filter(node -> !sourceNodeIds.contains(node.getId()))
			.forEach(node -> addEdgeToGraph(stateGraph, node.getId(), END));

		return stateGraph;
	}

	private static void addNodeToGraph(StateGraph<NodeState> graph, WorkflowNode<?> node,
			WorkFlowContext workFlowContext) throws GraphStateException {
		try {
			graph.addNode(node.getId(), node_async((state) -> Map.of("outputs", node.run(workFlowContext, state),
					"nodeName", node.getLabel(), "next", "else")));
		}
		catch (GraphStateException e) {
			throw new GraphStateException("添加节点失败: " + node.getId());
		}
	}

	private static void addEdgeToGraph(StateGraph<NodeState> graph, String source, String target) {
		try {
			graph.addEdge(source, target);
		}
		catch (GraphStateException e) {
			throw new RuntimeException(String.format("添加直接边失败: from %s to %s", source, target), e);
		}
	}

	private static void addConditionalEdgesToGraph(StateGraph<NodeState> graph, String sourceNode,
			Map<String, String> mapping) {
		try {
			graph.addConditionalEdges(sourceNode, edge_async(state -> state.data().get("next").toString()), mapping);
		}
		catch (GraphStateException e) {
			throw new RuntimeException("添加条件边失败: from " + sourceNode, e);
		}
	}

}