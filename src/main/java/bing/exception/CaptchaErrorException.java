package bing.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码输入错误异常
 * 
 * @author IceWee
 */
public class CaptchaErrorException extends AuthenticationException {

	private static final long serialVersionUID = -2201433718481497702L;

	public CaptchaErrorException(String msg) {
		super(msg);
	}

	public CaptchaErrorException(String msg, Throwable t) {
		super(msg, t);
	}

}
