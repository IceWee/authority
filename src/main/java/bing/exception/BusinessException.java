package bing.exception;

/**
 * 业务异常，如合法性校验
 * 
 * @author IceWee
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 321592438426914748L;

	protected String code;

	public BusinessException(String code) {
		this.code = code;
	}

	public BusinessException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public BusinessException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
