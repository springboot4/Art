package com.art.ai.dao.dataobject;

import com.art.ai.core.constants.ModelProtocolType;
import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fxz
 * @date 2025-10-09
 */
@Data
@TableName("ai_model_platform")
public class AiModelPlatformDO extends BaseEntity {

	private static final long serialVersionUID = -1L;

	/** 主键 */
	private Long id;

	/** 厂商名称 */
	private String name;

	/** base_url */
	private String baseUrl;

	/** api_key */
	private String apiKey;

	/** secret_key */
	private String secretKey;

	/** 是否启用代理 */
	private Integer proxyEnable;

	/** 是否兼容openapi协议 */
	private Integer openaiApiCompatible;

	/** 租户id */
	private Long tenantId;

	public boolean isProxyEnabled() {
		return Integer.valueOf(1).equals(proxyEnable);
	}

	public boolean isOpenAiCompatible() {
		return Integer.valueOf(1).equals(openaiApiCompatible);
	}

	public ModelProtocolType protocolType() {
		return isOpenAiCompatible() ? ModelProtocolType.OPENAI_COMPATIBLE : ModelProtocolType.VENDOR_NATIVE;
	}

	public String resolvedBaseUrl() {
		return baseUrl == null ? null : baseUrl.trim();
	}

}
