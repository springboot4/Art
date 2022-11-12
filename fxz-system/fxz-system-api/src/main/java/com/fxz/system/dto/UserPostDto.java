package com.fxz.system.dto;

import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;

/**
 * 用户与岗位关联表
 *
 * @author fxz
 * @date 2022-04-05
 */
@Data
public class UserPostDto extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	private Long userId;

	private Long postId;

}