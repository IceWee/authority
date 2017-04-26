package bing.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import bing.util.ExceptionUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handler(final Throwable throwable, final ModelMap model) {
		String errorMessage = ExceptionUtils.parseStackTrace(throwable);
		LOGGER.error("Exception during execution of Spring application", errorMessage);
		model.addAttribute("errorMessage", errorMessage);
		return "error";
	}

}
