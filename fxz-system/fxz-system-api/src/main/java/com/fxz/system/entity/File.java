package com.fxz.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fxz.common.mp.base.MpEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文件管理
 *
 * @author fxz
 * @date 2022-04-04
 */
@Data
@TableName("sys_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class File extends MpEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 桶名称
	 */
	private String bucketName;

	/**
	 * 原始文件名
	 */
	private String original;

	/**
	 * 文件类型
	 */
	private String type;

	/**
	 * 文件大小
	 */
	private Long fileSize;

}
