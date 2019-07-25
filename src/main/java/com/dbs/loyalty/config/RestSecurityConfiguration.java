package com.dbs.loyalty.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import com.dbs.loyalty.security.rest.RestAccessDeniedHandler;
import com.dbs.loyalty.security.rest.RestAccessFilter;
import com.dbs.loyalty.security.rest.RestTokenProvider;
import com.dbs.loyalty.security.rest.RestUnauthorizedEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Order(1)
@Configuration
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    private final RestTokenProvider restTokenProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception{
    	http
	        .formLogin().disable()
	        .httpBasic().disable()
	    	.headers().frameOptions().disable();
	    
	    http
	    	.csrf().disable();
	    
	    http
	    	.sessionManagement()
	        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    
	    http
	    	.antMatcher("/api/**")
	    		.authorizeRequests()
	    		.antMatchers("/api/authenticate").permitAll()
	    		.antMatchers("/api/customers/activate").permitAll()
	    		.antMatchers("/api/verification-tokens/**").permitAll()
	    		.anyRequest().authenticated();
	    
	    http
	    	.requestCache()
	    	.requestCache(new NullRequestCache());
	    
	    http
	   		.exceptionHandling()
	     	.authenticationEntryPoint(new RestUnauthorizedEntryPoint(objectMapper))
	     	.accessDeniedHandler(new RestAccessDeniedHandler(objectMapper));
	
	    http
	    	.addFilterBefore(new RestAccessFilter(restTokenProvider), UsernamePasswordAuthenticationFilter.class);
	    }

}
