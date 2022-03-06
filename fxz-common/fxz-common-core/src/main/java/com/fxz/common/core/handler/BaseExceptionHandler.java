package com.fxz.common.core.handler;

import com.fxz.common.core.entity.FxzResponse;
import com.fxz.common.core.exception.FxzAuthException;
import com.fxz.common.core.exception.FxzException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-28 11:42
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

	/**
	 * 统一处理请求参数校验(实体对象传参)
	 * @param e BindException
	 * @return FxzResponse
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public FxzResponse handleBindException(BindException e) {
		StringBuilder message = new StringBuilder();
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		for (FieldError error : fieldErrors) {
			message.append(error.getField()).append(error.getDefaultMessage()).append(",");
		}
		message = new StringBuilder(message.substring(0, message.length() - 1));
		return new FxzResponse().message(message.toString());
	}

	/**
	 * 统一处理请求参数校验(普通传参)
	 * @param e ConstraintViolationException
	 * @return FxzResponse
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public FxzResponse handleConstraintViolationException(ConstraintViolationException e) {
		StringBuilder message = new StringBuilder();
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			Path path = violation.getPropertyPath();
			String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
			message.append(pathArr[1]).append(violation.getMessage()).append(",");
		}
		message = new StringBuilder(message.substring(0, message.length() - 1));
		return new FxzResponse().message(message.toString());
	}

	@ExceptionHandler(value = FxzException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public FxzResponse handleFxzException(FxzException e) {
		log.error("系统错误", e);
		return new FxzResponse().message(e.getMessage());
	}

	@ExceptionHandler(value = FxzAuthException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public FxzResponse handleFxzAuthException(FxzAuthException e) {
		log.error("系统错误", e);
		return new FxzResponse().message(e.getMessage());
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public FxzResponse handleException(Exception e) {
		log.error("系统内部异常，异常信息", e);
		return new FxzResponse().message("系统内部异常");
	}

}
