package bing.security;

import bing.constant.GlobalConstants;
import bing.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * 自定义访问决策管理器，主要实现URL访问的权限控制
 *
 * @author IceWee
 */
@Slf4j
public class CustomAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        String url = StringUtils.EMPTY;
        if (object instanceof FilterInvocation) {
            FilterInvocation invocation = (FilterInvocation) object;
            url = invocation.getRequestUrl();
        }
        if (authentication == null) {
            log.warn("未认证的资源[{}]访问，禁止访问...", url);
            throw new AccessDeniedException("(403) Access denied.");
        }
        // 超级管理员：放行
        if (GlobalConstants.ADMIN.equals(authentication.getName())) {
            return;
        }
        // 如果当前资源未配置权限则禁止访问，即黑名单，否则为白名单都可以访问，显然是不对的
        if (configAttributes.isEmpty()) {
            log.warn("当前访问的资源[{}]未配置访问权限，默认禁止访问...", url);
            throw new AccessDeniedException("(403) Access denied.");
        }
        log.debug("访问当前资源[{}]应该具备的角色集合：[{}]", url, JsonUtils.toString(configAttributes));
        // 访问当前资源需要具备的角色ID集合
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.debug("当前用户：[{}]，具备的角色集合：[{}]", authentication.getName(), JsonUtils.toString(authentication));
        boolean matched = false;
        String needRoleId;
        String ownRoleId;
        loop:
        for (ConfigAttribute configAttribute : configAttributes) {
            needRoleId = configAttribute.getAttribute();
            for (GrantedAuthority authority : authorities) {
                ownRoleId = authority.getAuthority();
                // 当前用户具备的角色ID列表, 任意匹配到一个即为有权限访问
                if (StringUtils.equals(ownRoleId, needRoleId)) {
                    matched = true;
                    break loop;
                }
            }
        }
        if (!matched) {
            log.warn("当前用户不具备访问当前资源[{}]的权限，禁止访问...", url);
            throw new AccessDeniedException("(403) Access denied.");
        }
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
