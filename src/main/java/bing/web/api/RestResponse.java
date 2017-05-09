package bing.web.api;

import org.apache.commons.lang3.StringUtils;

/**
 * Rest接口返回类
 * 
 * @author IceWee
 */
public class RestResponse<T> {

	protected String code = RestResponseCodes.OK;
	protected String message = StringUtils.EMPTY;
	protected T data;
	protected long timestamp;

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
		if (timestamp == 0) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
