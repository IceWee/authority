package bing.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;

import bing.exception.BusinessExceptionCodes;
import bing.i18n.MessageSourceService;
import bing.util.AjaxUtils;

/**
 * 自定义Session过期处理策略
 * 
 * @author IceWee
 */
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomInvalidSessionStrategy.class);

	private final String destinationUrl;
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private boolean createNewSession = true;

	private MessageSourceService messageSourceService;

	public CustomInvalidSessionStrategy(String destinationUrl, MessageSourceService messageSourceService) {
		super();
		this.destinationUrl = destinationUrl;
		this.messageSourceService = messageSourceService;
	}

	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (createNewSession) {
			request.getSession();
		}
		if (AjaxUtils.ajaxRequest(request)) {
			LOGGER.info("ajax请求session过期...");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			LOGGER.info("普通请求session过期...");
			redirectStrategy.sendRedirect(request, response, destinationUrl);
		}
	}

	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
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
