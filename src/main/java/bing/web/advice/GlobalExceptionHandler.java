package bing.web.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import bing.constant.Charsets;
import bing.constant.GlobalConstants;
import bing.exception.BusinessException;
import bing.exception.BusinessExceptionCodes;
import bing.i18n.MessageSourceService;
import bing.util.AjaxUtils;
import bing.util.ExceptionUtils;

/**
 * 全局异常拦截器，遇到不可预料异常统一返回error页面 对于ajax异步请求会返回相应业务异常代码及错误信息，如果也是不可预料的异常则返回默认异常编码
 * 
 * @author IceWee
 */
@ControllerAdvice
public class GlobalExceptionHandler extends SimpleMappingExceptionResolver {

	private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	private MessageSourceService messageSourceService;

	@ExceptionHandler(value = Throwable.class)
	public String handler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		response.setCharacterEncoding(Charsets.CHARSET_UTF_8);
		LOGGER.error(ExceptionUtils.parseStackTrace(e));
		if (AjaxUtils.ajaxRequest(request)) {
			ajaxResponse(response, e);
			return null;
		} else {
			return error(request, e);
		}
	}

	/**
	 * 返回错误页面
	 * 
	 * @param request
	 * @param cause
	 * @return
	 */
	private String error(HttpServletRequest request, Exception cause) {
		String code = BusinessExceptionCodes.singleton().SERVER_ERROR;
		if (cause instanceof BusinessException) {
			BusinessException be = (BusinessException) cause;
			code = be.getCode();
		}
		String error = retriveMessage(code);
		String stack = ExceptionUtils.parseStackTrace(cause);
		request.setAttribute(GlobalConstants.REQUEST_ATTRIBUTE_ERROR, error);
		request.setAttribute(GlobalConstants.REQUEST_ATTRIBUTE_STACK, stack);
		return "error";
	}

	/**
	 * ajax返回
	 * 
	 * @param response
	 * @param cause
	 */
	private void ajaxResponse(HttpServletResponse response, Exception cause) {
		String code = BusinessExceptionCodes.singleton().SERVER_ERROR;
		if (cause instanceof BusinessException) {
			BusinessException be = (BusinessException) cause;
			code = be.getCode();
		}
		AjaxUtils.ajaxResponse(response, code, retriveMessage(code));
	}

	/**
	 * 根据编码进行国际化
	 * 
	 * @param code
	 * @return
	 */
	private String retriveMessage(String code) {
		String message;
		try {
			message = messageSourceService.getMessage(code);
		} catch (Exception ex) {
			LOGGER.warn("国际化文件中未配置错误编码：{}，返回未知错误提示", code);
			message = messageSourceService.getMessage(BusinessExceptionCodes.singleton().UNKNOW_ERROR);
		}
		return message;
	}

}
