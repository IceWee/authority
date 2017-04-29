package bing;

import org.apache.catalina.connector.Connector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import bing.constants.GlobalConstants;

@SpringBootApplication
@EnableCaching
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = GlobalConstants.SESSION_TIMEOUT_SECONDS) // session过期时间，单位：秒
@ComponentScan(basePackages = {GlobalConstants.COMPONENT_SCAN_PACKAGES})
@MapperScan(GlobalConstants.MAPPER_SCAN_PACKAGES)
public class AuthorityApplication {

	@Autowired
	private MessageSource messageSource;

	public static void main(String[] args) {
		new SpringApplicationBuilder(AuthorityApplication.class).web(true).run(args);
	}

	/**
	 * 该Bean的用途：默认校验框架会在classpath下寻找名称前缀为ValidationMessages的资源文件，
	 * 重新定义mvcValidator后可以修改默认资源文件名称及路径，使用Spring Boot的MessageSource搜索资源文件
	 * 
	 * @return
	 */
	@Bean(name = "mvcValidator")
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
				ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/403.html");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
				container.addErrorPages(error401Page, error403Page, error404Page, error500Page);
			}
		};
	}

	// 下面的两个Bean是用来重定向HTTP请求到HTTPS，注释后即可关闭重定向

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {

			// @Override
			// protected void postProcessContext(Context context) {
			// SecurityConstraint constraint = new SecurityConstraint();
			// constraint.setUserConstraint("CONFIDENTIAL");
			// SecurityCollection collection = new SecurityCollection();
			// collection.addPattern("/*");
			// constraint.addCollection(collection);
			// context.addConstraint(constraint);
			// }

		};

		// tomcat.addAdditionalTomcatConnectors(httpConnector());
		tomcat.setSessionTimeout(5);
		return tomcat;
	}

	@Bean
	public Connector httpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		// Connector监听的http的端口号
		connector.setPort(8080);
		connector.setSecure(false);
		// 监听到http的端口号后转向到的https的端口号
		connector.setRedirectPort(8443);
		return connector;
	}

}
