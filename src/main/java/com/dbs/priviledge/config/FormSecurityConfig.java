package com.dbs.priviledge.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import com.dbs.priviledge.security.ApiAuthenticationProvider;
import com.dbs.priviledge.security.AuthenticationFailureHandler;
import com.dbs.priviledge.security.AuthenticationSuccessHandler;
import com.dbs.priviledge.security.FormAuthenticationProvider;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class FormSecurityConfig extends WebSecurityConfigurerAdapter {

	private FormAuthenticationProvider formAuthenticationProvider;
	
	private ApiAuthenticationProvider apiAuthenticationProvider;
	
	private FindByIndexNameSessionRepository<?> sessionRepository;
	
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	private AuthenticationFailureHandler authenticationFailureHandler;

	public FormSecurityConfig(
			@Qualifier("formAuthenticationProvider") FormAuthenticationProvider formAuthenticationProvider, 
			@Qualifier("apiAuthenticationProvider") ApiAuthenticationProvider apiAuthenticationProvider, 
			FindByIndexNameSessionRepository<?> sessionRepository,
			AuthenticationSuccessHandler authenticationSuccessHandler, 
			AuthenticationFailureHandler authenticationFailureHandler) {
		
		this.formAuthenticationProvider = formAuthenticationProvider;
		this.apiAuthenticationProvider = apiAuthenticationProvider;
		this.sessionRepository = sessionRepository;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
    		.csrf().disable()
    		.headers().frameOptions().disable();
    
	    http
	    	.sessionManagement()
	    		.invalidSessionUrl("/login?logout")
	        	.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	        	.maximumSessions(5)
	        	.expiredUrl("/login?expired")
	        	.sessionRegistry(sessionRegistry())
	        	.maxSessionsPreventsLogin(true);
    
	    http
    		.authorizeRequests()
    		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    		.antMatchers("/static/**").permitAll()
    		.antMatchers("/login").permitAll()
    		.antMatchers("/authenticate").permitAll()
    		.antMatchers("/**").authenticated();
		
	    http
	    	.formLogin()
	    		.loginPage("/login")
	    		.loginProcessingUrl("/login")
	    		.usernameParameter("email")
	    		.passwordParameter("password")
	    		.successHandler(authenticationSuccessHandler)
	    		.failureHandler(authenticationFailureHandler);
	    
	    http
	    	.logout()
	    		.deleteCookies()
	    		.invalidateHttpSession(true)
	    		.clearAuthentication(true)
	    		.logoutUrl("/logout");
	    
	    http
	    	.exceptionHandling()
	    		.accessDeniedPage("/login?forbidden");
	
	}
	
	@Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}
	
	@Bean
	public SpringSessionBackedSessionRegistry<?> sessionRegistry() {
    	return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }
	
	@Bean
	public AuthenticationManager authenticationManager() {
	    return new ProviderManager(Arrays.asList(apiAuthenticationProvider, formAuthenticationProvider));
	}
	
}