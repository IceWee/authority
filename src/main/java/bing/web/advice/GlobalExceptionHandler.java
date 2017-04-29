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

import bing.constants.Charsets;
import bing.exception.BusinessException;
import bing.exception.BusinessExceptionCodes;
import bing.i18n.MessageSourceService;
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
		if (isAjax(request)) {
			ajax(response, e);
			return null;
		} else {
			return "error";
		}
	}

	/**
	 * ajax返回
	 * 
	 * @param response
	 * @param cause
	 */
	private void ajax(HttpServletResponse response, Exception cause) {
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
			e.printStackTrace();
		}
	}

	/**
	 * 解析HTTP头判断该请求是否是ajax
	 * 
	 * @param request
	 * @return
	 */
	private boolean isAjax(HttpServletRequest request) {
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
		String message = StringUtils.EMPTY;
		try {
			message = messageSourceService.getMessage(code);
		} catch (Exception e) {
			LOGGER.warn("请及时向codes.properties文件中补充错误编码：{}", code);
		}
		return message;
	}

}
