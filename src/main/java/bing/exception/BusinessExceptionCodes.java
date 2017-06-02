package bing.exception;

public abstract class BusinessExceptionCodes {

	/**
	 * 系统异常错误编码前缀
	 */
	private static final String PREFIX = "SYS_";

	/**
	 * 服务器内部错误
	 */
	public static final String SERVER_ERROR = PREFIX + "001";

	/*
	 * 未知错误，一般是业务异常，国际化时候未配置异常编码就会默认显示该错误
	 */
	public static final String UNKNOW_ERROR = PREFIX + "002";

	/**
	 * 系统管理员admin账号丢失
	 */
	public static final String USER_ADMIN_MISSING = PREFIX + "003";

	/**
	 * 系统管理员角色admin丢失
	 */
	public static final String ROLE_ADMIN_MISSING = PREFIX + "004";

	/**
	 * 文件上传失败
	 */
	public static final String UPLOAD_FAILED = PREFIX + "005";

	/**
	 * 无访问权限
	 */
	public static final String ACCESS_DENIED = PREFIX + "403";

	protected BusinessExceptionCodes() {
		super();
	}

}
