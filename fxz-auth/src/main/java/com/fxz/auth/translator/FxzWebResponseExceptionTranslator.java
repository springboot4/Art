package com.fxz.auth.translator;

import com.fxz.common.mp.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 覆盖默认的认证异常响应 需要在认证服务器配置
 *
 * @author Fxz
 * @version 1.0
 * @date 2021-11-27 21:17
 */

@Slf4j
@Component
@SuppressWarnings("all")
public class FxzWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

	@Override
	public ResponseEntity translate(Exception e) {
		ResponseEntity.BodyBuilder status = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		String message = "认证失败";
		log.error(message, e);
		if (e instanceof UnsupportedGrantTypeException) {
			message = "不支持该认证类型";
			return status.body(Result.failed(message));
		}
		if (e instanceof InvalidGrantException) {
			if (StringUtils.containsIgnoreCase(e.getMessage(), "Invalid refresh token")) {
				message = "refresh token无效";
				return status.body(Result.failed(message));
			}
			if (StringUtils.containsIgnoreCase(e.getMessage(), "locked")) {
				message = "用户已被锁定，请联系管理员";
				return status.body(Result.failed(message));
			}
			message = "用户名或密码错误";
			return status.body(Result.failed(message));
		}
		return status.body(Result.failed(message));
	}

}
