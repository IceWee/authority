package bing.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import bing.constants.RestResponseCodes;
import bing.domain.RestException;
import bing.domain.RestResponse;
import bing.util.ExceptionUtils;

@RestControllerAdvice
public class RestExceptionHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(value = RestException.class)
	public RestResponse<Object> handler(final RestException e) {
		String error = ExceptionUtils.parseStackTrace(e);
		LOGGER.error("接口调用异常，详细信息：\n{}", error);
		RestResponse<Object> response = new RestResponse<>();
		response.setCode(RestResponseCodes.REST_ERROR);
		response.setMessage(error);
		return response;
	}

}
