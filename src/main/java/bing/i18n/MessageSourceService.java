package bing.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Service
public class MessageSourceService {

    @Resource
    private MessageSource messageSource;

    public String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessageWithLocale(locale, code, args);
    }

    public String getMessage(HttpServletRequest request, String code, Object... args) {
        Locale locale = RequestContextUtils.getLocale(request);
        return getMessageWithLocale(locale, code, args);
    }

    public String getMessageWithLocale(Locale locale, String code, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

}
