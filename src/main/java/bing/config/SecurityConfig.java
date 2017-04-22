package bing.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	@Qualifier("sysUserService")
	private UserDetailsService userDetailsService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/i18n/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOGGER.info("********************* Spring Security*************************");
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers("/login").permitAll()
			.anyRequest().authenticated()
			.and().formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/main").permitAll()
			.and().logout().permitAll()
			// 开启cookie保存用户数据
			.and().rememberMe()
			// 设置cookie有效期
			.tokenValiditySeconds(60 * 60 * 24 * 7)
			// 设置cookie的私钥
			.key("authority_");
		// 设置注销成功后跳转页面，默认是跳转到登录页面
		http.logout().logoutSuccessUrl("/login?logout");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(new Md5PasswordEncoder());
		authProvider.setUserDetailsService(userDetailsService);
		// 盐策略：通过反射UserDetails实现类的属性
		ReflectionSaltSource saltSource = new ReflectionSaltSource();
		// 盐取值为用户属性-salt
		saltSource.setUserPropertyToUse("salt");
		authProvider.setSaltSource(saltSource);
		auth.authenticationProvider(authProvider);
	}

}
