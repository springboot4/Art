package com.fxz.system.dto;

import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-04-05
 */
@Data
public class PostDto extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	private Long postId;

	private String postCode;

	private String postName;

	private Integer postSort;

	private String delFlag;

	private String description;

}