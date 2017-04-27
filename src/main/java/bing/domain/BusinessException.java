package bing.domain;

/**
 * 业务异常，如合法性校验
 * 
 * @author IceWee
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 321592438426914748L;

	private int code;

	public BusinessException(int code) {
		this.code = code;
	}

	public BusinessException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public BusinessException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
