package com.fxz.system.dto;

import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;

/**
 * 操作日志记录
 *
 * @author fxz
 * @date 2022-04-07
 */
@Data
public class OperLogDto extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	private Long id;

	private String title;

	private Integer businessType;

	private String method;

	private String requestMethod;

	private String operName;

	private String operUrl;

	private String operIp;

	private String operParam;

	private Integer status;

	private String errorMsg;

	private Long time;

}