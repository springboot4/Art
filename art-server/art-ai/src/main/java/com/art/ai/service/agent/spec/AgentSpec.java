package com.art.ai.service.agent.spec;

import com.art.ai.service.agent.tool.ToolArgumentBinding;
import com.art.ai.service.workflow.domain.node.NodeInputVariable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Agent 规格模型
 *
 * @author fxz
 * @since 2025-11-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentSpec {

	@JsonProperty("version")
	private String version;

	@JsonProperty("modelId")
	private String modelId;

	@JsonProperty("platformId")
	private String platformId;

	@JsonProperty("temperature")
	@Builder.Default
	private Double temperature = 0.7;

	@JsonProperty("maxTokens")
	private Integer maxTokens;

	@JsonProperty("language")
	@Builder.Default
	private String language = "zh";

	@JsonProperty("systemPrompt")
	private String systemPrompt;

	@JsonProperty("strategy")
	@Builder.Default
	private Strategy strategy = new Strategy();

	@JsonProperty("tools")
	@Builder.Default
	private List<String> tools = new ArrayList<>();

	@JsonProperty("toolBindings")
	@Builder.Default
	private Map<String, List<ToolArgumentBinding>> toolBindings = new HashMap<>();

	@JsonProperty("toolOutputMode")
	@Builder.Default
	private ToolOutputMode toolOutputMode = ToolOutputMode.JSON_MODE;

	@JsonProperty("knowledge")
	@Builder.Default
	private Knowledge knowledge = new Knowledge();

	@JsonProperty("memory")
	@Builder.Default
	private Memory memory = new Memory();

	@JsonProperty("budgets")
	@Builder.Default
	private Budgets budgets = new Budgets();

	@JsonProperty("extensions")
	@Builder.Default
	private Map<String, Object> extensions = new HashMap<>();

	@JsonProperty("metadata")
	@Builder.Default
	private Map<String, Object> metadata = new HashMap<>();

	/**
	 * 用户输入变量定义 定义 Agent 运行时需要从用户获取的输入变量
	 */
	@JsonProperty("userInputs")
	@Builder.Default
	private List<NodeInputVariable> userInputs = new ArrayList<>();

	@JsonIgnore
	public List<ToolArgumentBinding> bindingsFor(String toolName) {
		if (toolBindings == null || toolName == null) {
			return Collections.emptyList();
		}

		List<ToolArgumentBinding> bindings = toolBindings.get(toolName);
		if (bindings == null || bindings.isEmpty()) {
			return Collections.emptyList();
		}

		return Collections.unmodifiableList(bindings);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Strategy {

		@JsonProperty("type")
		@Builder.Default
		private StrategyType type = StrategyType.REACT;

		@JsonProperty("params")
		@Builder.Default
		private Map<String, Object> params = new HashMap<>();

		@JsonProperty("decorators")
		@Builder.Default
		private List<String> decorators = Collections.emptyList();

	}

	public enum StrategyType {

		@JsonProperty("react")
		REACT,

		@JsonProperty("plan_execute")
		PLAN_EXECUTE,

	}

	public enum ToolOutputMode {

		@JsonProperty("json_mode")
		JSON_MODE,

		@JsonProperty("native")
		NATIVE,

		@JsonProperty("prompt")
		PROMPT

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Knowledge {

		@JsonProperty("datasetIds")
		@Builder.Default
		private List<String> datasetIds = Collections.emptyList();

		@JsonProperty("retrievalTypes")
		@Builder.Default
		private List<String> retrievalTypes = Collections.emptyList();

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Memory {

		@JsonProperty("enabled")
		@Builder.Default
		private Boolean enabled = Boolean.TRUE;

		@JsonProperty("window")
		@Builder.Default
		private Window window = new Window();

		@JsonProperty("summary")
		@Builder.Default
		private Summary summary = new Summary();

		@JsonIgnoreProperties(ignoreUnknown = true)
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Window {

			@JsonProperty("size")
			@Builder.Default
			private Integer size = 10;

		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Summary {

			@JsonProperty("enabled")
			@Builder.Default
			private Boolean enabled = Boolean.FALSE;

		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Budgets {

		@JsonProperty("maxSteps")
		@Builder.Default
		private Integer maxSteps = 8;

		@JsonProperty("maxTimeMs")
		@Builder.Default
		private Long maxTimeMs = 30000L;

		@JsonProperty("maxToolCalls")
		@Builder.Default
		private Integer maxToolCalls = 8;

		@JsonProperty("allowParallelTools")
		@Builder.Default
		private Boolean allowParallelTools = Boolean.FALSE;

	}

}
