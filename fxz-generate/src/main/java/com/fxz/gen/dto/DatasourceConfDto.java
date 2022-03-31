package com.fxz.gen.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 数据源表
 *
 * @author fxz
 * @date 2022-03-31
 */
@Data
@Accessors(chain = true)
public class DatasourceConfDto {

	private static final long serialVersionUID = -3090644297573721386L;

	private Long id;

	private String name;

	private String url;

	private String username;

	private String password;

	private String delFlag;

	private LocalDateTime createTime;

	private String createBy;

	private LocalDateTime updateTime;

	private String updateBy;

}