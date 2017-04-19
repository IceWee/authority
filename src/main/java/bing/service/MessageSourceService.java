package bing.service;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

@Service
public class MessageSourceService {

	@Resource
	private MessageSource messageSource;

	public String getMessage(String code) {
		return getMessage(code, null);
	}

	public String getMessage(String code, Object[] args) {
		Locale locale = LocaleContextHolder.getLocale();
		return getMessageWithLocale(code, args, locale);
	}

	public String getMessage(String code, Object[] args, HttpServletRequest request) {
		Locale locale = RequestContextUtils.getLocale(request);
		return getMessageWithLocale(code, args, locale);
	}

	public String getMessageWithLocale(String code, Locale locale) {
		return getMessageWithLocale(code, null, locale);
	}

	public String getMessageWithLocale(String code, Object[] args, Locale locale) {
		return messageSource.getMessage(code, args, locale);
	}

}
