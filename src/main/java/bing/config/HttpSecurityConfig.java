package bing.config;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;

import bing.security.CustomAccessDecisionManager;
import bing.security.CustomAuthenticationProvider;
import bing.security.CustomInvalidSessionStrategy;
import bing.security.CustomSecurityMetadataSource;
import bing.security.SecurityConstants;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpSecurityConfig.class);

	@Autowired
	@Qualifier("customAuthenticationDetailsSource")
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

	@Autowired
	@Qualifier("authorizeService")
	private UserDetailsService userDetailsService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	@Qualifier("customAccessDeniedHandler")
	private AccessDeniedHandler accessDeniedHandler;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	/**
	 * 配置公共访问资源路径，以下路径无需登录，不拦截
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(SecurityConstants.PUBLIC_RESOURCE_PATHS);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOGGER.info("********************* HttpSecurityConfig *************************");
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers(SecurityConstants.URI_LOGIN).permitAll()
				// 任何其他的请求都需要授权
				.anyRequest().authenticated()
				// 配置登录地址，默认也是/login，登录失败地址、登录成功后跳转到的地址，这几个地址都无需授权即可访问
				.and().formLogin().loginPage(SecurityConstants.URI_LOGIN).failureUrl(SecurityConstants.URI_LOGIN_FAILURE).defaultSuccessUrl(SecurityConstants.URI_LOGIN_SUCCESS).permitAll()
				// 自定义权限源，实现验证码
				.authenticationDetailsSource(authenticationDetailsSource).and().logout().permitAll()
				// 开启cookie保存用户数据
				.and().rememberMe().rememberMeParameter(SecurityConstants.PARAM_REMEMBER_ME)
				// 设置cookie存放方案为数据库
				.tokenRepository(persistentTokenRepository())
				// 设置cookie有效期
				.tokenValiditySeconds(SecurityConstants.COOKIE_EXPIRE_SECONDS)
				// 设置cookie的私钥
				.key(SecurityConstants.COOKIE_KEY)
				// 设置拒绝访问页面
				.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler).accessDeniedPage(SecurityConstants.URI_ACCESS_DENIED);

		http.authorizeRequests().anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

			/**
			 * 注入自定义的访问决策管理器和自定义权限资源
			 */
			@Override
			public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
				fsi.setAccessDecisionManager(accessDecisionManager());
				fsi.setSecurityMetadataSource(securityMetadataSource());
				return fsi;
			}

		});

		// 设置注销成功后跳转页面，默认是跳转到登录页面
		http.logout().logoutSuccessUrl(SecurityConstants.URI_LOGOUT_SUCCESS);

		// session过期定制
		http.sessionManagement().maximumSessions(SecurityConstants.MAXIMUM_SESSIONS).expiredUrl(SecurityConstants.URI_SESSION_EXPIRED).and().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.invalidSessionUrl(SecurityConstants.URI_SESSION_EXPIRED);
		http.sessionManagement().invalidSessionStrategy(invalidSessionStrategy());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setStringRedisTemplate(stringRedisTemplate);
		auth.authenticationProvider(authenticationProvider);
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		tokenRepositoryImpl.setDataSource(dataSource);
		return tokenRepositoryImpl;
	}

	@Bean
	public AccessDecisionManager accessDecisionManager() {
		return new CustomAccessDecisionManager();
	}

	@Bean
	public FilterInvocationSecurityMetadataSource securityMetadataSource() {
		return new CustomSecurityMetadataSource();
	}

	@Bean
	public InvalidSessionStrategy invalidSessionStrategy() {
		return new CustomInvalidSessionStrategy(SecurityConstants.URI_SESSION_EXPIRED);
	}

}
