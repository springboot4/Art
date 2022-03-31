package com.fxz.gen.param;

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
public class DatasourceConfParam {

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