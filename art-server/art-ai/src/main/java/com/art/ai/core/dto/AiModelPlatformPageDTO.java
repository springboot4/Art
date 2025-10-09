package com.art.ai.core.dto;

import com.art.core.common.model.BasePageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fxz
 * @date 2025-10-09
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "")
@Data
public class AiModelPlatformPageDTO extends BasePageEntity {

	private static final long serialVersionUID = -1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "厂商名称")
	private String name;

	@Schema(description = "base_url")
	private String baseUrl;

	@Schema(description = "api_key")
	private String apiKey;

	@Schema(description = "secret_key")
	private String secretKey;

	@Schema(description = "是否启用代理")
	private Integer proxyEnable;

	@Schema(description = "是否兼容openapi协议")
	private Integer openaiApiCompatible;

	@Schema(description = "")
	private Long tenantId;

}