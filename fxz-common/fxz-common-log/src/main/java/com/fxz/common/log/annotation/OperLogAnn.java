package com.fxz.common.log.annotation;

import com.fxz.common.log.enums.BusinessType;

import java.lang.annotation.*;

/**
 * 操作日志记录注解
 *
 * @author fxz
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLogAnn {

	/**
	 * 模块
	 */
	public String title() default "";

	/**
	 * 功能
	 */
	public BusinessType businessType() default BusinessType.OTHER;

	/**
	 * 是否保存请求的参数
	 */
	public boolean isSaveRequestData() default true;

}
