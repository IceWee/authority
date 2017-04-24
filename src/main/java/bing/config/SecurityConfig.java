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
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import bing.constants.SystemConstants;
import bing.security.CustomAuthenticationProvider;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	@Qualifier("customAuthenticationDetailsSource")
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

	@Autowired
	@Qualifier("sysUserService")
	private UserDetailsService userDetailsService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/i18n/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOGGER.info("********************* Spring Security*************************");
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers("/login").permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/main").permitAll()
				// 自定义权限源，实现验证码
				.authenticationDetailsSource(authenticationDetailsSource).and().logout().permitAll()
				// 开启cookie保存用户数据
				.and().rememberMe().rememberMeParameter(SystemConstants.PARAM_REMEMBER_ME).tokenRepository(persistentTokenRepository()).tokenValiditySeconds(86400)
				// 设置cookie有效期
				.tokenValiditySeconds(60 * 60 * 24 * 7)
				// 设置cookie的私钥
				.key("authority_");

		// 设置注销成功后跳转页面，默认是跳转到登录页面
		http.logout().logoutSuccessUrl("/login?logout");

		// session配置
		http.sessionManagement().maximumSessions(1).expiredUrl("/login?expired").and().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).invalidSessionUrl("/login?expired");
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

}
