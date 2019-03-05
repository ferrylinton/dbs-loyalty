package com.dbs.loyalty.config;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.dbs.loyalty.util.UrlUtil;
import com.github.bufferings.thymeleaf.extras.nl2br.dialect.Nl2brDialect;

@Configuration
public class WebConfiguration {

	public WebConfiguration(ServletContext context) {
		UrlUtil.contextPath = context.getContextPath();
	}
	
	@Bean("localeResolver")
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(new Locale("in"));
		return resolver;
	}
	
	@Bean
	public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource);
	    return bean;
	}

	@Bean
	public Nl2brDialect dialect() {
	  return new Nl2brDialect();
	}

}
