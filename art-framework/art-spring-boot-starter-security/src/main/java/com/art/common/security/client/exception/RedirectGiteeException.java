package com.art.common.security.client.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class RedirectGiteeException extends OAuth2AuthenticationException {

	public RedirectGiteeException(String errorCode) {
		super(errorCode);
	}

	public RedirectGiteeException(OAuth2Error error) {
		super(error);
	}

	public RedirectGiteeException(OAuth2Error error, Throwable cause) {
		super(error, cause);
	}

	public RedirectGiteeException(OAuth2Error error, String message) {
		super(error, message);
	}

	public RedirectGiteeException(OAuth2Error error, String message, Throwable cause) {
		super(error, message, cause);
	}

	@Override
	public OAuth2Error getError() {
		return super.getError();
	}

}
