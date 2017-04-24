package bing.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 自定义权限资源，它的职责是访问一个url时返回这个url所需要的访问权限
 * 
 * @author IceWee
 */
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	/**
	 * 返回所有定义的权限资源，一般不需要检查直接返回null
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	/**
	 * 返回本次访问需要的权限
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		Collection<ConfigAttribute> configAttributes = new ArrayList<>();
		FilterInvocation fi = (FilterInvocation) object;
		// TODO 后续需要编写根据URI获取需要的全部权限的方法
		Map<String, Collection<ConfigAttribute>> metadataSource = CustomSecurityContext.getMetadataSource();
		// key - uri, value - 权限集合
		for (Map.Entry<String, Collection<ConfigAttribute>> entry : metadataSource.entrySet()) {
			String uri = entry.getKey();
			RequestMatcher requestMatcher = new AntPathRequestMatcher(uri);
			if (requestMatcher.matches(fi.getHttpRequest())) {
				configAttributes.addAll(entry.getValue());
			}
		}
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
