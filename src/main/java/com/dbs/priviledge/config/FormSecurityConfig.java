package com.dbs.priviledge.config;

import java.util.Arrays;

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
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.dbs.priviledge.security.ApiAuthenticationProvider;
import com.dbs.priviledge.security.AuthenticationFailureHandler;
import com.dbs.priviledge.security.AuthenticationSuccessHandler;
import com.dbs.priviledge.security.FormAuthenticationProvider;
import com.dbs.priviledge.service.CustomerService;
import com.dbs.priviledge.service.UserService;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class FormSecurityConfig extends WebSecurityConfigurerAdapter {
	
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
    		.antMatchers("/h2-console/**").permitAll()
    		.antMatchers("/login").permitAll()
    		.antMatchers("/authenticate").permitAll()
    		.antMatchers("/**").authenticated();
		
	    http
	    	.formLogin()
	    		.loginPage("/login")
	    		.loginProcessingUrl("/login")
	    		.usernameParameter("email")
	    		.passwordParameter("password")
	    		.successHandler(new AuthenticationSuccessHandler("/home"))
	    		.failureHandler(new AuthenticationFailureHandler("/login?error"));
	    
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
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(UserService userService, CustomerService customerService) {
	    return new ProviderManager(Arrays.asList(new FormAuthenticationProvider(userService), new ApiAuthenticationProvider(customerService)));
	}
 
}