package com.fxz.common.core.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Fxz
 * @version 1.0
 * @date 2022/10/25 11:57
 */
@UtilityClass
public class WebUtil {

	/**
	 * 获取HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return Optional.ofNullable(getRequestAttributes()).map(ServletRequestAttributes::getRequest).orElse(null);
	}

	/**
	 * 获取RequestAttributes
	 */
	public ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

}
