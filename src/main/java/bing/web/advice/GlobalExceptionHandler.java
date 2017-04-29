package bing.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import bing.util.ExceptionUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = Throwable.class)
	public String handler(final Throwable e, Model model) {
		String error = ExceptionUtils.parseStackTrace(e);
		LOGGER.error("服务器内部错误，详细信息：\n{}", error);
		model.addAttribute("error", error);
		return "error";
	}

}
