package bing.system.exception;

import bing.exception.BusinessExceptionCodes;
import io.swagger.annotations.ApiModelProperty;

public class RoleExceptionCodes extends BusinessExceptionCodes {

	private static class SingletonHolder {
		private static final RoleExceptionCodes INSTANCE = new RoleExceptionCodes();
	}

	/**
	 * 角色异常错误编码前缀
	 */
	private static final String PREFIX = "ROLE_";

	/**
	 * 角色编码已被使用
	 */
	@ApiModelProperty(value = "角色编码已被使用", dataType = "String", required = true, position = 1, example = "ROLE_001")
	public final String CODE_USED = PREFIX + "001";

	/**
	 * 角色编码禁止修改
	 */
	@ApiModelProperty(value = "角色编码禁止修改", dataType = "String", required = true, position = 2, example = "ROLE_002")
	public final String CODE_FORBIDDEN_MODIFY = PREFIX + "002";

	/**
	 * 角色已被授权给用户禁止删除
	 */
	@ApiModelProperty(value = "角色已被授权给用户禁止删除", dataType = "String", required = true, position = 3, example = "ROLE_003")
	public final String AUTHORIZED_TO_USER = PREFIX + "003";

	protected RoleExceptionCodes() {
		super();
	}

	public static RoleExceptionCodes singleton() {
		return SingletonHolder.INSTANCE;
	}

}
