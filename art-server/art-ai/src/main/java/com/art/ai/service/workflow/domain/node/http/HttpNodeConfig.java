package com.art.ai.service.workflow.domain.node.http;

import com.art.ai.service.workflow.domain.node.NodeConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fxz
 * @since 2025/8/10 16:38
 */
@Data
public class HttpNodeConfig extends NodeConfig {

	/**
	 * 请求地址
	 */
	private String url;

	/**
	 * 请求体（post、put时携带 对应：contentType为application/json）
	 */
	private HttpRequestBody body;

	/**
	 * get、post、put、delete
	 */
	private String method;

	/**
	 * 请求参数
	 */
	private List<HttpParamDefinition> params;

	/**
	 * 请求头
	 */
	private List<HttpParamDefinition> headers;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class HttpParamDefinition {

		private String key;

		private String value;

		private boolean enabled = true;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class HttpRequestBody {

		/**
		 * 请求体类型 json、formData
		 */
		private String type;

		/**
		 * formData类型的请求体
		 */
		private List<HttpParamDefinition> formData;

		/**
		 * json类型的请求体
		 */
		private String jsonData;

	}

}
