package bing.system.exception;

import bing.exception.BusinessExceptionCodes;

public class ResourceExceptionCodes extends BusinessExceptionCodes {

	/**
	 * 资源异常错误编码前缀
	 */
	private static final String PREFIX = "RESOURCE_";

	/**
	 * Url已被使用
	 */
	public static final String URL_USED = PREFIX + "001";

	/**
	 * Url禁止修改
	 */
	public static final String URL_FORBIDDEN_MODIFY = PREFIX + "002";

	private ResourceExceptionCodes() {
		super();
	}

}
