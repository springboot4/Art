package com.fxz.gen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 数据源表
 *
 * @author fxz
 * @date 2022-03-31
 */
@Data
@Accessors(chain = true)
@TableName("gen_datasource_conf")
@EqualsAndHashCode(callSuper = true)
public class DatasourceConf extends BaseEntity {

	private static final long serialVersionUID = 666L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 数据源名称
	 */
	private String name;

	/**
	 * jdbc-url
	 */
	private String url;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 删除标记
	 */
	@TableLogic
	private String delFlag;

}
