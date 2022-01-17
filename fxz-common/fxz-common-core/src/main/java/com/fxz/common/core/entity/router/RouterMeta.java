package com.fxz.common.core.entity.router;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fxz
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouterMeta implements Serializable {

	private static final long serialVersionUID = 5499925008927195914L;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 图标
	 */
	private String icon;

}