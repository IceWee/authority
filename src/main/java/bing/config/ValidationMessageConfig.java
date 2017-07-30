package bing.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * JSR303与Spring国际化集成<br>
 * 作用：默认校验框架会在classpath下寻找名称前缀为ValidationMessages的资源文件，
 * 重新定义mvcValidator后可以修改默认资源文件名称及路径，使用Spring Boot的MessageSource搜索资源文件
 * 
 * @author IceWee
 */
//@Configuration
public class ValidationMessageConfig {

	@Bean
	public LocalValidatorFactoryBean mvcValidator(MessageSource messageSource) {
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
		factory.setValidationMessageSource(messageSource);
		return factory;
	}

}
