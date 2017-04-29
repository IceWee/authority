package bing.web.api;

import org.apache.commons.lang3.StringUtils;

/**
 * Rest接口返回类
 * 
 * @author IceWee
 */
public class RestResponse<T> {

	private String code = "200";
	private String message = StringUtils.EMPTY;
	private T data;
	private long timestamp;

	public RestResponse() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
