package bing.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import bing.system.service.AuthorizeService;
import bing.util.JsonUtils;

/**
 * 自定义权限资源，它的职责是访问一个url时返回这个url所需要的访问权限
 * 
 * @author IceWee
 */
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomSecurityMetadataSource.class);

	@Autowired
	@Qualifier("authorizeService")
	private AuthorizeService authorizeService;

	/**
	 * 返回所有定义的权限资源，一般不需要检查直接返回null
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		Collection<ConfigAttribute> configAttributes = new ArrayList<>();
		FilterInvocation fi = (FilterInvocation) object;
		List<URISecurityConfigs> uriSecurityConfigs = authorizeService.listURISecurityConfigs();
		System.out.println(JsonUtils.toString(uriSecurityConfigs));
		uriSecurityConfigs.forEach(uriConfigs -> {
			String uri = uriConfigs.getUri();
			RequestMatcher requestMatcher = new AntPathRequestMatcher(uri);
			if (requestMatcher.matches(fi.getHttpRequest())) {
				configAttributes.addAll(uriConfigs.getConfigAttributes());
			}
		});
		LOGGER.debug("访问当前资源需要具备的角色集合：[{}]", JsonUtils.toString(configAttributes));
		return configAttributes;
	}

	/**
	 * 返回类对象是否支持校验，web项目一般使用FilterInvocation来判断，或者直接返回true
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
