package com.art.ai.dao.dataobject;

import com.art.mybatis.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fxz
 * @date 2025-07-25
 */
@Data
@TableName("ai_app")
public class AiAppDO extends BaseEntity {

	private static final long serialVersionUID = -1L;

	private Long id;

	private String name;

	private String description;

	private String mode;

	private String icon;

	private String status;

	private Long tenantId;

}
