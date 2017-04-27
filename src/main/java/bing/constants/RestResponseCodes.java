package bing.constants;

/**
 * Rest返回码全局定义类
 * 
 * @author IceWee
 */
public class RestResponseCodes {

	public static final int OK = 200;

	public static final int SERVER_ERROR = 500;

	/**
	 * 参数未通过校验
	 */
	public static final int PARAM_INVALID = 600;

	/**
	 * RESTFul接口异常
	 */
	public static final int REST_ERROR = 700;

	private RestResponseCodes() {
		super();
	}

}
