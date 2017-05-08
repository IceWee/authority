package bing.security;

import org.springframework.security.authentication.BadCredentialsException;

public class CaptchaException extends BadCredentialsException {

	private static final long serialVersionUID = 6619482196898021350L;

	public CaptchaException(String msg) {
		super(msg);
	}

}
