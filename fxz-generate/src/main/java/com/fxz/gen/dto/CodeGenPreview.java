package com.fxz.gen.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 代码预览内容
 *
 * @author Fxz
 * @version 1.0
 * @date 2022-03-03 15:38
 */
@Data
@Accessors(chain = true)
public class CodeGenPreview {

	/**
	 * 代码名称
	 */
	private String name;

	/**
	 * 代码内容
	 */
	private String content;

}
