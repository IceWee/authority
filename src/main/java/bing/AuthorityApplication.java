package bing;

import bing.constant.GlobalConstants;
import bing.security.SecurityConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// @EnableCaching
@SpringBootApplication(scanBasePackages = { GlobalConstants.COMPONENT_SCAN_PACKAGES })
@EnableAsync // 开启异步调用
@EnableScheduling // 允许定时任务
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = SecurityConstants.SESSION_TIMEOUT_SECONDS) // session过期时间，单位：秒
@MapperScan(GlobalConstants.MAPPER_SCAN_PACKAGES)
public class AuthorityApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AuthorityApplication.class).web(true).run(args);
	}

}
