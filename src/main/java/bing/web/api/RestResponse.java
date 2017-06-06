package bing.web.api;

import org.apache.commons.lang3.StringUtils;

import io.swagger.annotations.ApiModelProperty;

/**
 * Rest接口返回类
 * 
 * @author IceWee
 */
public class RestResponse<T> {

	@ApiModelProperty(value = "状态码", required = true, dataType = "String", example = "200")
	protected String code = RestResponseCodes.OK;

	@ApiModelProperty(value = "错误消息", dataType = "String", example = "请求失败")
	protected String message = StringUtils.EMPTY;

	@ApiModelProperty(value = "返回数据")
	protected T data;

	@ApiModelProperty(value = "时间戳", required = true, dataType = "Long", example = "1496755507770")
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
