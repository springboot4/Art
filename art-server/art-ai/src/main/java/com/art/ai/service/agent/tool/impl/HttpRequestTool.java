package com.art.ai.service.agent.tool.impl;

import com.art.ai.service.agent.tool.AgentTool;
import com.art.ai.service.agent.tool.AgentToolDefinition;
import com.art.ai.service.agent.tool.AgentToolException;
import com.art.ai.service.agent.tool.AgentToolRequest;
import com.art.ai.service.agent.tool.AgentToolResult;
import com.art.ai.service.agent.tool.ToolArgumentDescriptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * http_request 工具
 *
 * @author fxz
 * @since 2025-11-01
 */
@Slf4j
@Component
public class HttpRequestTool implements AgentTool {

	public static final String NAME = "http_request";

	private static final int DEFAULT_CONNECT_TIMEOUT_MS = 5000;

	private static final int DEFAULT_READ_TIMEOUT_MS = 15000;

	private static final int BODY_LIMIT = 4000;

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	public HttpRequestTool(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MS);
		factory.setReadTimeout(DEFAULT_READ_TIMEOUT_MS);
		this.restTemplate = new RestTemplate(factory);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public AgentToolDefinition definition() {
		return AgentToolDefinition.builder()
			.name(NAME)
			.description("执行基础 HTTP 请求，支持 GET/POST/PATCH/PUT/DELETE")
			.timeout(Duration.ofSeconds(20))
			.arguments(List.of(
					ToolArgumentDescriptor.builder()
						.name("url")
						.type("string")
						.required(true)
						.description("目标请求地址，需为合法 URL")
						.build(),
					ToolArgumentDescriptor.builder()
						.name("method")
						.type("string")
						.required(false)
						.description("HTTP 方法，默认 GET，可选 POST/PUT/PATCH/DELETE")
						.build(),
					ToolArgumentDescriptor.builder()
						.name("headers")
						.type("object")
						.required(false)
						.description("HTTP 请求头，键值均为字符串")
						.build(),
					ToolArgumentDescriptor.builder()
						.name("body")
						.type("string|object")
						.required(false)
						.description("请求体，字符串或 JSON 对象；GET 一般无需 body")
						.build()))
			.build();
	}

	@Override
	public AgentToolResult invoke(AgentToolRequest request) throws AgentToolException {
		long start = System.nanoTime();
		JsonNode args = request.getArguments();
		if (args == null || !args.hasNonNull("url")) {
			throw new AgentToolException("http_request 缺少 url 参数");
		}

		String url = args.get("url").asText();
		String methodStr = args.hasNonNull("method") ? args.get("method").asText() : "GET";
		HttpMethod method = HttpMethod.valueOf(methodStr.toUpperCase(Locale.ROOT));
		HttpHeaders headers = buildHeaders(args);
		String body = extractBody(args);

		try {
			RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, method, URI.create(url));
			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			Map<String, Object> data = new HashMap<>();
			data.put("status", response.getStatusCode().value());
			data.put("headers", sanitizeHeaders(response.getHeaders()));
			String truncatedBody = response.getBody();
			data.put("body", truncatedBody);
			long elapsed = (System.nanoTime() - start) / 1_000_000;

			return AgentToolResult.builder()
				.ok(true)
				.data(data)
				.elapsedMs(elapsed)
				.truncated(truncatedBody != null && truncatedBody.length() >= BODY_LIMIT)
				.preview(buildPreview(method, url, response))
				.build();
		}
		catch (RestClientResponseException ex) {
			Map<String, Object> data = new HashMap<>();
			data.put("status", ex.getStatusCode().value());
			data.put("headers", sanitizeHeaders(ex.getResponseHeaders()));
			data.put("body", ex.getResponseBodyAsString());
			return AgentToolResult.builder()
				.ok(false)
				.data(data)
				.errorCode("HTTP_ERROR")
				.errorMessage(ex.getMessage())
				.build();
		}
		catch (RestClientException ex) {
			throw new AgentToolException("HTTP 请求失败: " + ex.getMessage(), ex);
		}
	}

	private HttpHeaders buildHeaders(JsonNode args) {
		HttpHeaders headers = new HttpHeaders();
		if (args != null && args.has("headers") && args.get("headers").isObject()) {
			args.get("headers")
				.fields()
				.forEachRemaining(entry -> headers.add(entry.getKey(), entry.getValue().asText()));
		}
		if (!headers.containsKey(HttpHeaders.ACCEPT_CHARSET)) {
			headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
		}
		return headers;
	}

	private String extractBody(JsonNode args) throws AgentToolException {
		if (args == null || !args.has("body")) {
			return null;
		}
		JsonNode bodyNode = args.get("body");
		if (bodyNode.isNull()) {
			return null;
		}
		if (bodyNode.isTextual()) {
			return bodyNode.asText();
		}
		try {
			return objectMapper.writeValueAsString(bodyNode);
		}
		catch (Exception ex) {
			throw new AgentToolException("无法序列化 body", ex);
		}
	}

	private Map<String, List<String>> sanitizeHeaders(HttpHeaders headers) {
		if (headers == null) {
			return Map.of();
		}
		Map<String, List<String>> sanitized = new HashMap<>();
		headers
			.forEach((key, value) -> sanitized.put(key, value != null ? value.stream().limit(5).toList() : List.of()));
		return sanitized;
	}

	private String truncateBody(String body) {
		if (StringUtils.isBlank(body)) {
			return body;
		}
		if (body.length() <= BODY_LIMIT) {
			return body;
		}
		return body.substring(0, BODY_LIMIT);
	}

	private String buildPreview(HttpMethod method, String url, ResponseEntity<String> response) {
		return method.name() + " " + url + " -> " + response.getStatusCode().value();
	}

}
