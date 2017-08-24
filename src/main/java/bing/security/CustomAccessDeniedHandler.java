package bing.security;

import bing.exception.BusinessExceptionCodes;
import bing.i18n.MessageSourceService;
import bing.util.AjaxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义访问拒绝处理类
 *
 * @author IceWee
 */
@Slf4j
@Component("customAccessDeniedHandler")
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

    @Autowired
    private MessageSourceService messageSourceService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (AjaxUtils.ajaxRequest(request)) {
            String code = BusinessExceptionCodes.singleton().ACCESS_DENIED;
            AjaxUtils.ajaxResponse(response, code, retriveMessage(code));
        } else {
            super.handle(request, response, accessDeniedException);
        }
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
            log.warn("国际化文件中未配置错误编码：{}，返回未知错误提示", code);
            message = messageSourceService.getMessage(BusinessExceptionCodes.singleton().UNKNOW_ERROR);
        }
        return message;
    }

}
