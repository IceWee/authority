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

	/**
	 * 分类下包含资源
	 */
	public static final String CATEGORY_CONTAINS_RESOURCE = PREFIX + "003";

	/**
	 * 包含子分类
	 */
	public static final String CATEGORY_CONTAINS_SUBCATEGORY = PREFIX + "004";

	/**
	 * 已被菜单使用
	 */
	public static final String USED_BY_MENU = PREFIX + "005";

	/**
	 * 已授权给角色
	 */
	public static final String AUTHORIZED_TO_ROLE = PREFIX + "006";

	private ResourceExceptionCodes() {
		super();
	}

}
