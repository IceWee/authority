package bing.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import bing.annotation.CurrentUser;
import bing.model.SysUser;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(currentUserMethodArgumentResolver());
		super.addArgumentResolvers(argumentResolvers);
	}

	@Bean
	public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
		return new CurrentUserMethodArgumentResolver();
	}

	/**
	 * CurrentUser注解处理器
	 * 
	 * @author IceWee
	 */
	public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

		@Override
		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
			Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (object instanceof SysUser) {
				SysUser user = (SysUser) object;
				if (user != null) {
					return user;
				}
			}
			throw new MissingServletRequestPartException("currentUser");
		}

		@Override
		public boolean supportsParameter(MethodParameter parameter) {
			return parameter.getParameterType().isAssignableFrom(SysUser.class) && parameter.hasParameterAnnotation(CurrentUser.class);
		}

	}

}
