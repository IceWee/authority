package bing.system.exception;

import bing.exception.BusinessExceptionCodes;

public class RoleExceptionCodes extends BusinessExceptionCodes {

	/**
	 * 角色异常错误编码前缀
	 */
	private static final String PREFIX = "ROLE_";

	/**
	 * 编码已被使用
	 */
	public static final String CODE_USED = PREFIX + "001";

	/**
	 * 编码禁止修改
	 */
	public static final String CODE_FORBIDDEN_MODIFY = PREFIX + "002";

	private RoleExceptionCodes() {
		super();
	}

}
