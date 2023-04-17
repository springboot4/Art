package com.art.common.security.client.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class SecretGiteeException extends OAuth2AuthenticationException {

	public SecretGiteeException(String errorCode) {
		super(errorCode);
	}

	public SecretGiteeException(OAuth2Error error) {
		super(error);
	}

	public SecretGiteeException(OAuth2Error error, Throwable cause) {
		super(error, cause);
	}

	public SecretGiteeException(OAuth2Error error, String message) {
		super(error, message);
	}

	public SecretGiteeException(OAuth2Error error, String message, Throwable cause) {
		super(error, message, cause);
	}

	@Override
	public OAuth2Error getError() {
		return super.getError();
	}

}
