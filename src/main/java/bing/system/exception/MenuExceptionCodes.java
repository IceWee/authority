package bing.system.exception;

import bing.exception.BusinessExceptionCodes;
import io.swagger.annotations.ApiModelProperty;

public class MenuExceptionCodes extends BusinessExceptionCodes {

	private static class SingletonHolder {
		private static final MenuExceptionCodes INSTANCE = new MenuExceptionCodes();
	}

	/**
	 * 菜单异常错误编码前缀
	 */
	private static final String PREFIX = "MENU_";

	/**
	 * 已包含子菜单禁止删除
	 */
	@ApiModelProperty(value = "已包含子菜单禁止删除", dataType = "String", required = true, position = 1, example = "MENU_001")
	public final String CONTAINS_SUBMENUS = PREFIX + "001";

	/**
	 * 顶级菜单禁止删除
	 */
	@ApiModelProperty(value = "顶级菜单禁止删除", dataType = "String", required = true, position = 2, example = "MENU_002")
	public final String TOP_MENU_FORBIDDEN_DELETE = PREFIX + "002";

	protected MenuExceptionCodes() {
		super();
	}

	public static MenuExceptionCodes singleton() {
		return SingletonHolder.INSTANCE;
	}

}
