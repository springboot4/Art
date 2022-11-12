package com.fxz.system.dto;

import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;

/**
 * 字典表
 *
 * @author fxz
 * @date 2022-04-04
 */
@Data
public class DictDto extends BaseCreateEntity {

	private static final long serialVersionUID = -7062810411163801969L;

	private Long id;

	private String type;

	private String description;

	private String remark;

	private String systemFlag;

	private String delFlag;

}