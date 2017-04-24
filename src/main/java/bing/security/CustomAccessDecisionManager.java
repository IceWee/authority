package bing.security;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

/**
 * 自定义访问决策管理器，主要实现URL访问的权限控制
 * 
 * @author IceWee
 */
public class CustomAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		Iterator<ConfigAttribute> iterator = configAttributes.iterator(); // 访问当前URI需要具备的权限列表
		while (iterator.hasNext()) {
			if (authentication == null) {
				throw new AccessDeniedException("当前访问没有权限");
			}
			ConfigAttribute configAttribute = iterator.next();
			String needCode = configAttribute.getAttribute();
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 当前用户具备的权限列表
			for (GrantedAuthority authority : authorities) {
				if (StringUtils.equals(authority.getAuthority(), needCode)) {
					return;
				}
			}
		}
		throw new AccessDeniedException("当前访问没有权限");
	}

	@Override
	public boolean supports(ConfigAttribute paramConfigAttribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
