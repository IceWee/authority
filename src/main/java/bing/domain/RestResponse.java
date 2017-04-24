package bing.domain;

import org.apache.commons.lang3.StringUtils;

import bing.constants.RestResponseCodes;

/**
 * Rest接口返回类
 * 
 * @author IceWee
 */
public class RestResponse<T> {

	private int code = RestResponseCodes.OK;
	private String message = StringUtils.EMPTY;
	private T data;
	private long timestamp;

	public RestResponse() {
		super();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
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
