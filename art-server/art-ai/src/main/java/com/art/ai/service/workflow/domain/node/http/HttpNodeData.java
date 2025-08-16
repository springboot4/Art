package com.art.ai.service.workflow.domain.node.http;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.art.ai.service.workflow.NodeState;
import com.art.ai.service.workflow.WorkFlowContext;
import com.art.ai.service.workflow.domain.node.NodeData;
import com.art.ai.service.workflow.domain.node.NodeOutputVariable;
import com.art.ai.service.workflow.domain.node.NodeProcessResult;
import com.art.ai.service.workflow.domain.node.WorkflowNode;
import com.art.ai.service.workflow.variable.VariableDataType;
import com.art.ai.service.workflow.variable.VariablePool;
import com.art.ai.service.workflow.variable.VariableRenderUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author fxz
 * @since 2025/8/10 16:38
 */
@Slf4j
@Data
public class HttpNodeData extends NodeData<HttpNodeConfig> {

	@Override
	public NodeProcessResult process(WorkFlowContext workFlowContext, NodeState nodeState) {
		List<NodeOutputVariable> nodeOutputVariables = Collections.emptyList();

		HttpNodeConfig config = getConfig();

		VariablePool variablePool = workFlowContext.getPool();
		Map<String, Object> inputs = initNodeInputsByReference(variablePool, config);
		String url = config.getUrl();

		List<HttpNodeConfig.HttpParamDefinition> params = config.getParams();
		if (CollectionUtil.isNotEmpty(params)) {
			for (HttpNodeConfig.HttpParamDefinition param : params) {
				String separator = url.contains("?") ? "&" : "?";
				url = url + separator + param.getKey() + "=" + param.getValue();
				url = VariableRenderUtils.format(url, inputs);
			}
		}

		Method httpMethod = Method.valueOf(config.getMethod());
		HttpRequest request = HttpRequest.of(url).method(httpMethod);

		List<HttpNodeConfig.HttpParamDefinition> headers = config.getHeaders();
		if (CollectionUtil.isNotEmpty(headers)) {
			for (HttpNodeConfig.HttpParamDefinition header : headers) {
				if (header.isEnabled() && header.getKey() != null && header.getValue() != null) {
					String value = header.getValue();
					String render = VariableRenderUtils.format(value, inputs);
					request.header(header.getKey(), render);
				}
			}
		}

		if (Method.POST.equals(httpMethod) || Method.PUT.equals(httpMethod)) {
			HttpNodeConfig.HttpRequestBody body = config.getBody();
			List<HttpNodeConfig.HttpParamDefinition> formData = body.getFormData();
			if (StrUtil.equalsAnyIgnoreCase("form-urlencoded", body.getType()) && CollectionUtil.isNotEmpty(formData)) {
				for (HttpNodeConfig.HttpParamDefinition paramNode : formData) {
					String value = paramNode.getValue();
					String render = VariableRenderUtils.format(value, inputs);
					request.form(paramNode.getKey(), render);
				}
			}

			String jsonBody = body.getJsonData();
			if (StrUtil.equalsAnyIgnoreCase("json", body.getType()) && StrUtil.isNotBlank(jsonBody)) {
				String renderedBody = VariableRenderUtils.format(jsonBody, inputs);
				request.body(renderedBody);
			}
		}

		try (HttpResponse response = request.execute()) {
			nodeOutputVariables = List.of(
					new NodeOutputVariable(WorkflowNode.NodeOutputConstants.HTTP_RESPONSE_STATUS,
							VariableDataType.NUMBER, response.getStatus()),
					new NodeOutputVariable(WorkflowNode.NodeOutputConstants.HTTP_RESPONSE_BODY, VariableDataType.STRING,
							response.body()));
		}
		catch (Exception e) {
			log.error("HTTP request failed: {}", e.getMessage(), e);
		}

		return NodeProcessResult.builder().outputVariables(nodeOutputVariables).build();
	}

}
