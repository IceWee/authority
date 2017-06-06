package bing.exception;

import io.swagger.annotations.ApiModelProperty;

public class BusinessExceptionCodes {

	private static class SingletonHolder {
		private static final BusinessExceptionCodes INSTANCE = new BusinessExceptionCodes();
	}

	/**
	 * 系统异常错误编码前缀
	 */
	private static final String PREFIX = "SYS_";

	/**
	 * 服务器内部错误
	 */
	@ApiModelProperty(value = "服务器内部错误", dataType = "String", required = true, position = 1, example = "SYS_001")
	public final String SERVER_ERROR = PREFIX + "001";

	/*
	 * 未知错误，一般是业务异常，国际化时候未配置异常编码就会默认显示该错误
	 */
	@ApiModelProperty(value = "未知错误", dataType = "String", required = true, position = 2, example = "SYS_002")
	public final String UNKNOW_ERROR = PREFIX + "002";

	/**
	 * 系统管理员admin账号丢失
	 */
	@ApiModelProperty(value = "系统管理员admin账号丢失", dataType = "String", required = true, position = 3, example = "SYS_003")
	public final String USER_ADMIN_MISSING = PREFIX + "003";

	/**
	 * 系统管理员角色admin丢失
	 */
	@ApiModelProperty(value = "系统管理员角色admin丢失", dataType = "String", required = true, position = 4, example = "SYS_004")
	public final String ROLE_ADMIN_MISSING = PREFIX + "004";

	/**
	 * 文件上传失败
	 */
	@ApiModelProperty(value = "文件上传失败", dataType = "String", required = true, position = 5, example = "SYS_005")
	public final String UPLOAD_FAILED = PREFIX + "005";

	/**
	 * 会话过期
	 */
	@ApiModelProperty(value = "会话过期", dataType = "String", required = true, position = 6, example = "SYS_006")
	public final String SESSION_EXPIRED = PREFIX + "006";

	/**
	 * 无访问权限
	 */
	@ApiModelProperty(value = "无访问权限", dataType = "String", required = true, position = 7, example = "SYS_403")
	public final String ACCESS_DENIED = PREFIX + "403";

	protected BusinessExceptionCodes() {
		super();
	}

	public static final BusinessExceptionCodes singleton() {
		return SingletonHolder.INSTANCE;
	}

}
