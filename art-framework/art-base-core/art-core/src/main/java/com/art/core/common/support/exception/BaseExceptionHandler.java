/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.core.common.support.exception;

import com.art.core.common.exception.ArtException;
import com.art.core.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author Fxz
 * @version 0.0.1
 * @date 2021-11-28 11:42
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

	/**
	 * 统一处理请求参数校验(实体对象传参)
	 * @param e BindException
	 * @return Result
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result<Void> handleBindException(BindException e) {
		StringBuilder message = new StringBuilder();
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		for (FieldError error : fieldErrors) {
			message.append(error.getField()).append(error.getDefaultMessage()).append(",");
		}
		message = new StringBuilder(message.substring(0, message.length() - 1));
		return Result.failed(message.toString());
	}

	/**
	 * 统一处理请求参数校验(普通传参)
	 * @param e ConstraintViolationException
	 * @return Result
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
		StringBuilder msg = new StringBuilder();
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			String message = constraintViolation.getMessage();
			msg.append(message).append(System.lineSeparator());
		}

		return Result.failed(msg.toString());
	}

	@ExceptionHandler(value = ArtException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Void> handleFxzException(ArtException e) {
		log.error("系统错误", e);
		return Result.failed(e.getLocalizedMessage());
	}

	@ExceptionHandler(value = { AccessDeniedException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Void> handleFxzAuthException(Exception e) {
		log.error("认证错误", e);
		return Result.failed("认证错误");
	}

	@ExceptionHandler(value = RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Void> handleRuntimeException(Exception e) {
		log.error("系统内部异常，异常信息", e);
		return Result.failed(e.getMessage());
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Void> handleException(Exception e) {
		log.error("系统内部异常，异常信息", e);
		return Result.failed("系统内部异常");
	}

}
