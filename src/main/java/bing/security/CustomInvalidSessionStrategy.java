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

	public CustomInvalidSessionStrategy(String destinationUrl) {
		super();
		this.destinationUrl = destinationUrl;
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

}