package bing.web.advice;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import bing.exception.BusinessException;
import bing.service.MessageSourceService;
import bing.util.ExceptionUtils;
import bing.web.api.RestResponse;

@RestControllerAdvice
public class BusinessExceptionHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionHandler.class);

	@Autowired
	private MessageSourceService messageSourceService;

	@ResponseBody
	@ExceptionHandler(value = BusinessException.class)
	public RestResponse<Object> handler(final BusinessException e) {
		String error = ExceptionUtils.parseStackTrace(e);
		LOGGER.error("接口调用异常，详细信息：\n{}", error);
		RestResponse<Object> response = new RestResponse<>();
		response.setCode(e.getCode());
		response.setMessage(retriveMessage(e.getCode()));
		return response;
	}

	/**
	 * 根据编码进行国际化
	 * 
	 * @param code
	 * @return
	 */
	private String retriveMessage(int code) {
		String message = StringUtils.EMPTY;
		try {
			message = messageSourceService.getMessage(String.valueOf(code));
		} catch (Exception e) {
			LOGGER.warn("请及时向codes.properties文件中补充错误编码：{}", code);
		}
		return message;
	}

}
