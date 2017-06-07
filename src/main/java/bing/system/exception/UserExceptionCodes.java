package bing.system.exception;

import bing.exception.BusinessExceptionCodes;
import io.swagger.annotations.ApiModelProperty;

public class UserExceptionCodes extends BusinessExceptionCodes {

	private static class SingletonHolder {
		private static final UserExceptionCodes INSTANCE = new UserExceptionCodes();
	}

	/**
	 * 用户异常错误编码前缀
	 */
	private static final String PREFIX = "USER_";

	/**
	 * 账号已被注册
	 */
	@ApiModelProperty(value = "账号已被注册", dataType = "String", required = true, position = 1, example = "USER_001")
	public final String USERNAME_REGISTERED = PREFIX + "001";

	/**
	 * 账号禁止修改
	 */
	@ApiModelProperty(value = "账号禁止修改", dataType = "String", required = true, position = 2, example = "USER_002")
	public final String USERNAME_FORBIDDEN_MODIFY = PREFIX + "002";

	/**
	 * 原始密码错误
	 */
	@ApiModelProperty(value = "原始密码错误", dataType = "String", required = true, position = 3, example = "USER_003")
	public final String ORIGION_PASSWORD_WRONG = PREFIX + "003";

	/**
	 * 姓名不能为空
	 */
	@ApiModelProperty(value = "姓名不能为空", dataType = "String", required = true, position = 4, example = "USER_004")
	public final String NAME_IS_NULL = PREFIX + "004";

	protected UserExceptionCodes() {
		super();
	}

	public static UserExceptionCodes singleton() {
		return SingletonHolder.INSTANCE;
	}

}
