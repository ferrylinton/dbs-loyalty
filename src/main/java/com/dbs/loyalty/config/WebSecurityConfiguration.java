package com.dbs.loyalty.config;

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
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.event.LoginEventPublisher;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.security.rest.RestAuthenticationProvider;
import com.dbs.loyalty.security.web.WebAuthenticationFailureHandler;
import com.dbs.loyalty.security.web.WebAuthenticationProvider;
import com.dbs.loyalty.security.web.WebAuthenticationSuccessHandler;
import com.dbs.loyalty.service.AuthenticateLdapService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private static final String LOGIN = "/login";
	
	private final LoginEventPublisher loginEventPublisher;
	
	private final ApplicationProperties applicationProperties;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
    		.csrf().disable()
    		.headers().frameOptions().disable();
    
	    http
	    	.sessionManagement()
	    		.invalidSessionUrl("/login?invalid")
	        	.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	        	.maximumSessions(1)
	        	.expiredUrl("/login?expired")
	        	.sessionRegistry(sessionRegistry())
	        	.maxSessionsPreventsLogin(true);
    
	    http
    		.authorizeRequests()
    		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    		.antMatchers("/static/**").permitAll()
    		.antMatchers("/webjars/**").permitAll()
    		.antMatchers("/h2-console/**").permitAll()
    		.antMatchers(LOGIN).permitAll()
    		.antMatchers("/swagger-ui.html").permitAll()
    		.antMatchers("/v2/api-docs").permitAll()
    		.antMatchers("/swagger-resources/configuration/ui").permitAll()
    		.antMatchers("/swagger-resources").permitAll()
    		.antMatchers("/swagger-resources/configuration/security").permitAll()
    		.antMatchers("/**").authenticated();
		
	    http
	    	.formLogin()
	    		.loginPage(LOGIN)
	    		.loginProcessingUrl(LOGIN)
	    		.successHandler(new WebAuthenticationSuccessHandler(loginEventPublisher))
	    		.failureHandler(new WebAuthenticationFailureHandler(loginEventPublisher));
	    
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
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(UserRepository userRepository, AuthenticateLdapService authenticateLdapService, CustomerRepository customerRepository) {
	    return new ProviderManager(Arrays.asList(webAuthenticationProvider(userRepository, authenticateLdapService), restAuthenticationProvider(customerRepository)));
	}
 
	@Bean("webAuthenticationProvider")
	public WebAuthenticationProvider webAuthenticationProvider(UserRepository userRepository, AuthenticateLdapService authenticateLdapService) {
		return new WebAuthenticationProvider(userRepository, authenticateLdapService, applicationProperties);
	}
	
	@Bean("restAuthenticationProvider")
	public RestAuthenticationProvider restAuthenticationProvider(CustomerRepository customerRepository){
		return new RestAuthenticationProvider(customerRepository);
	}
	
	@Bean  
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {  
	    return new GrantedAuthorityDefaults(Constant.EMPTY); // Remove the ROLE_ prefix  
	}
	
}