package bing.exception;

public abstract class BusinessExceptionCodes {

	/**
	 * 服务器内部错误
	 */
	public static final String SERVER_ERROR = "500";

	/*
	 * 未知错误，一般是业务异常，国际化时候未配置异常编码就会默认显示该错误
	 */
	public static final String UNKNOW_ERROR = "600";

	protected BusinessExceptionCodes() {
		super();
	}

}
