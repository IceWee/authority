package bing.system.exception;

import bing.exception.BusinessExceptionCodes;

public class UserExceptionCodes extends BusinessExceptionCodes {

	/**
	 * 用户异常错误编码前缀
	 */
	private static final String PREFIX = "USER_";

	/**
	 * 账号已被注册
	 */
	public static final String USERNAME_REGISTERED = PREFIX + "001";

	/**
	 * 账号禁止修改
	 */
	public static final String USERNAME_FORBIDDEN_MODIFY = PREFIX + "002";

	private UserExceptionCodes() {
		super();
	}

}
