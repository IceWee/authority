package bing;

//import org.apache.catalina.connector.Connector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import bing.constant.GlobalConstants;
import bing.security.SecurityConstants;

// @EnableCaching
@SpringBootApplication(scanBasePackages = { GlobalConstants.COMPONENT_SCAN_PACKAGES })
@EnableAsync // 开启异步调用
@EnableScheduling // 允许定时任务
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = SecurityConstants.SESSION_TIMEOUT_SECONDS) // session过期时间，单位：秒
@MapperScan(GlobalConstants.MAPPER_SCAN_PACKAGES)
// public class AuthorityApplication extends SpringBootServletInitializer { //
// 打war包时打开
public class AuthorityApplication { // 开发时打开

	/**
	 * 实现SpringBootServletInitializer可以让spring-boot项目在web容器中运行
	 */
	// @Override // 打war包时打开
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AuthorityApplication.class);
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(AuthorityApplication.class).web(true).run(args);
	}

	// @Bean
	// public EmbeddedServletContainerCustomizer containerCustomizer() {
	// return new EmbeddedServletContainerCustomizer() {
	// @Override
	// public void customize(ConfigurableEmbeddedServletContainer container) {
	// ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED,
	// "/401.html");
	// ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN,
	// "/403.html");
	// ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND,
	// "/404.html");
	// container.addErrorPages(error401Page, error403Page, error404Page);
	// }
	// };
	// }

	// 下面的两个Bean是用来重定向HTTP请求到HTTPS，注释后即可关闭重定向

	// @Bean
	// public EmbeddedServletContainerFactory servletContainer() {
	// TomcatEmbeddedServletContainerFactory tomcat = new
	// TomcatEmbeddedServletContainerFactory() {
	//
	// // 打开注释开启HTTPS，同时需要解开配置文件中ssl开头的配置，并将server端口改为8443
	// @Override
	// protected void postProcessContext(Context context) {
	// SecurityConstraint constraint = new SecurityConstraint();
	// constraint.setUserConstraint("CONFIDENTIAL");
	// SecurityCollection collection = new SecurityCollection();
	// collection.addPattern("/*");
	// constraint.addCollection(collection);
	// context.addConstraint(constraint);
	// }
	//
	// };
	//
	// tomcat.addAdditionalTomcatConnectors(httpConnector()); //
	// // 打开注释开启HTTP转发到HTTPS
	// tomcat.setSessionTimeout(5);
	// return tomcat;
	// }

	// @Bean
	// public Connector httpConnector() {
	// Connector connector = new
	// Connector("org.apache.coyote.http11.Http11NioProtocol");
	// connector.setScheme("http");
	// // Connector监听的http的端口号
	// connector.setPort(8080);
	// connector.setSecure(false);
	// // 监听到http的端口号后转向到的https的端口号
	// connector.setRedirectPort(8443);
	// return connector;
	// }

}
