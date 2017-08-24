package bing.security;

import bing.util.AjaxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义Session过期处理策略
 *
 * @author IceWee
 */
@Slf4j
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

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
            log.info("ajax请求session过期...");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            log.info("普通请求session过期...");
            redirectStrategy.sendRedirect(request, response, destinationUrl);
        }
    }

    public void setCreateNewSession(boolean createNewSession) {
        this.createNewSession = createNewSession;
    }

}