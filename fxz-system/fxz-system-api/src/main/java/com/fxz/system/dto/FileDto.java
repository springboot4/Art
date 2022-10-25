package com.fxz.system.dto;

import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文件管理
 *
 * @author fxz
 * @date 2022-04-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class FileDto extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	private Long id;

	private String fileName;

	private String bucketName;

	private String original;

	private String type;

	private Long fileSize;

	private String delFlag;

}