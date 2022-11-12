package com.fxz.system.dto;

import com.fxz.common.mp.base.BaseCreateEntity;
import lombok.Data;

/**
 * 文件管理
 *
 * @author fxz
 * @date 2022-04-04
 */
@Data
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