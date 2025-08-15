package com.art.ai.service.workflow;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bsc.langgraph4j.state.AgentState;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 17:11
 */
@ToString
@Getter
@Setter
public class NodeState extends AgentState implements Serializable {

	public NodeState(Map<String, Object> initData) {
		super(initData);
	}

	public NodeState() {
		super(new HashMap<>());
	}

}
