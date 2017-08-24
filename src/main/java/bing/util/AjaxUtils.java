package bing.util;

import bing.constant.Charsets;
import bing.web.api.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class AjaxUtils {

    private static final String HTTP_HEADER_ACCEPT = "accept";
    private static final String X_REQUESTED_WITH = "X-Requested-With";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    /**
     * 解析HTTP头判断该请求是否是ajax
     *
     * @param request
     * @return
     */
    public static boolean ajaxRequest(HttpServletRequest request) {
        String accept = request.getHeader(HTTP_HEADER_ACCEPT);
        if (StringUtils.contains(accept, MediaType.APPLICATION_JSON_VALUE)) {
            return true;
        }
        String xmlHttp = request.getHeader(X_REQUESTED_WITH);
        if (StringUtils.isNotBlank(xmlHttp) && StringUtils.contains(xmlHttp, XML_HTTP_REQUEST)) {
            return true;
        }
        return false;
    }

    /**
     * ajax返回
     *
     * @param response
     * @param code
     * @param message
     */
    public static void ajaxResponse(HttpServletResponse response, String code, String message) {
        try {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding(Charsets.CHARSET_UTF_8);
            RestResponse<Object> restResponse = new RestResponse<>();
            restResponse.setCode(code);
            restResponse.setMessage(message);
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtils.toString(restResponse));
            writer.flush();
        } catch (IOException e) {
            log.error(ExceptionUtils.parseStackTrace(e));
        }
    }

}
