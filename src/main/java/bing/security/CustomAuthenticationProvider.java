package bing.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;

import bing.constant.RedisKeys;
import bing.util.PasswordUtils;

/**
 * 自定义权限认证处理类，加入了验证码校验
 * 
 * @author IceWee
 */
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	private boolean forcePrincipalAsString = false;
	private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
	private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
	private UserDetailsService userDetailsService;
	private StringRedisTemplate stringRedisTemplate;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			LOGGER.debug("Authentication failed: no credentials provided");
			throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		// 使用新版密码加密工具栏验证登陆密码
		String rawPassword = authentication.getCredentials().toString();
		if (!PasswordUtils.match(rawPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");
			throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		UserDetails loadedUser = null;
		try {
			loadedUser = userDetailsService.loadUserByUsername(username);
		} catch (UsernameNotFoundException notFound) {
			throw notFound;
		} catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}
		if (loadedUser == null) {
			throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
		}
		return loadedUser;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
		String captcha = details.getCaptcha();
		LOGGER.info("用户输入的登陆验证码：{}", captcha);
		String currentSessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		String rawCpatcha = stringRedisTemplate.opsForValue().get(RedisKeys.PREFIX_CAPTCHA + currentSessionId);
		LOGGER.info("已缓存的登录验证码：{}", rawCpatcha);
		stringRedisTemplate.opsForValue().getOperations().delete(RedisKeys.PREFIX_CAPTCHA + currentSessionId);
		if (!StringUtils.equalsIgnoreCase(rawCpatcha, captcha)) {
			LOGGER.warn("验证码输入错误，需跳转到登录页面重新输入");
			throw new CaptchaException("验证码错误");
		}
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		UserDetails user = null;
		try {
			user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
		} catch (UsernameNotFoundException notFound) {
			LOGGER.debug("User '" + username + "' not found");
			if (hideUserNotFoundExceptions) {
				throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
			} else {
				throw notFound;
			}
		}

		Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");

		try {
			preAuthenticationChecks.check(user);
			additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
		} catch (AuthenticationException exception) {
			throw exception;
		}

		postAuthenticationChecks.check(user);

		Object principalToReturn = user;
		if (isForcePrincipalAsString()) {
			principalToReturn = user.getUsername();
		}

		return createSuccessAuthentication(principalToReturn, authentication, user);
	}

	@Override
	public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
		this.forcePrincipalAsString = forcePrincipalAsString;
	}

	@Override
	public boolean isForcePrincipalAsString() {
		return forcePrincipalAsString;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
		@Override
		public void check(UserDetails user) {
			if (!user.isAccountNonLocked()) {
				LOGGER.debug("User account is locked");
				throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
			}
			if (!user.isEnabled()) {
				LOGGER.debug("User account is disabled");
				throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
			}
			if (!user.isAccountNonExpired()) {
				LOGGER.debug("User account is expired");
				throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
			}
		}
	}

	private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
		@Override
		public void check(UserDetails user) {
			if (!user.isCredentialsNonExpired()) {
				LOGGER.debug("User account credentials have expired");
				throw new CredentialsExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
			}
		}
	}

}
