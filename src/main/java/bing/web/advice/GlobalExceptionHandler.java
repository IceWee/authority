package bing.web.advice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import bing.constant.ApiConstants;
import bing.constant.Charsets;
import bing.constant.GlobalConstants;
import bing.exception.BusinessException;
import bing.exception.BusinessExceptionCodes;
import bing.i18n.MessageSourceService;
import bing.util.ExceptionUtils;
import bing.util.JsonUtils;
import bing.web.api.RestResponse;

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
		if (apiRequest(request) || ajaxRequest(request)) {
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
		String code = BusinessExceptionCodes.SERVER_ERROR;
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
		try {
			String code = BusinessExceptionCodes.SERVER_ERROR;
			if (cause instanceof BusinessException) {
				BusinessException be = (BusinessException) cause;
				code = be.getCode();
			}
			RestResponse<Object> restResponse = new RestResponse<>();
			restResponse.setCode(code);
			restResponse.setMessage(retriveMessage(code));
			PrintWriter writer = response.getWriter();
			writer.write(JsonUtils.toString(restResponse));
			writer.flush();
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.parseStackTrace(e));
		}
	}

	/**
	 * 匹配请求URL判断是否是API请求
	 * 
	 * @param request
	 * @return
	 */
	private boolean apiRequest(HttpServletRequest request) {
		return StringUtils.startsWith(request.getServletPath(), ApiConstants.API_URL_PREFIX);
	}

	/**
	 * 解析HTTP头判断该请求是否是ajax
	 * 
	 * @param request
	 * @return
	 */
	private boolean ajaxRequest(HttpServletRequest request) {
		String accept = request.getHeader("accept");
		if (StringUtils.contains(accept, MediaType.APPLICATION_JSON_VALUE)) {
			return true;
		}
		String xmlHttp = request.getHeader("X-Requested-With");
		if (StringUtils.isNotBlank(xmlHttp) && StringUtils.contains(xmlHttp, "XMLHttpRequest")) {
			return true;
		}
		return false;
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
			message = messageSourceService.getMessage(BusinessExceptionCodes.UNKNOW_ERROR);
		}
		return message;
	}

}
