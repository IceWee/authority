package bing.exception;

public class UserExceptionCodes extends BusinessExceptionCodes {

	/**
	 * 用户异常错误编码前缀
	 */
	private static final String PREFIX = "USER_";

	/**
	 * 用户名已被注册
	 */
	public static final String USERNAME_REGISTERED = PREFIX + "001";

	private UserExceptionCodes() {
		super();
	}

}
