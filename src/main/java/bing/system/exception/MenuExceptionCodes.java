package bing.system.exception;

import bing.exception.BusinessExceptionCodes;

public class MenuExceptionCodes extends BusinessExceptionCodes {

	/**
	 * 菜单异常错误编码前缀
	 */
	private static final String PREFIX = "MENU_";

	/**
	 * 包含子菜单
	 */
	public static final String CONTAINS_SUBMENUS = PREFIX + "001";

	/**
	 * 顶级菜单禁止删除
	 */
	public static final String TOP_MENU_FORBIDDEN_DELETE = PREFIX + "002";

}
