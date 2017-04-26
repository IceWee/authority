package bing.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import bing.constants.RestResponseCodes;
import bing.domain.RestResponse;
import bing.util.ExceptionUtils;

@RestControllerAdvice
public class RestExceptionHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

	@ExceptionHandler(value = Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public RestResponse<Object> handler(final Throwable e) {
		String error = ExceptionUtils.parseStackTrace(e);
		LOGGER.error("服务器内部错误（500）：\n{}", error);
		RestResponse<Object> response = new RestResponse<>();
		response.setCode(RestResponseCodes.SERVER_ERROR);
		response.setMessage(error);
		return response;
	}

}
