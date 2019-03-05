package com.dbs.loyalty.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import com.dbs.loyalty.security.rest.RestAccessDeniedHandler;
import com.dbs.loyalty.security.rest.RestAccessFilter;
import com.dbs.loyalty.security.rest.RestLoginFilter;
import com.dbs.loyalty.security.rest.RestTokenProvider;
import com.dbs.loyalty.security.rest.RestUnauthorizedEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;

@Order(1)
@Configuration
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final Logger LOG = LoggerFactory.getLogger(RestSecurityConfiguration.class);
	
    private final ObjectMapper objectMapper;

    private RestTokenProvider jwtService;

    public RestSecurityConfiguration(ObjectMapper objectMapper, RestTokenProvider jwtService) {
    	this.objectMapper = objectMapper;
		this.jwtService = jwtService;
	}

    @Override
    public void configure(HttpSecurity http){
    	try {
            http
	            .formLogin().disable()
	            .httpBasic().disable()
            	.csrf().disable()
            	.headers().frameOptions().disable();
            
            http
            	.sessionManagement()
                	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            
            http
            	.antMatcher("/api/**")
		    		.authorizeRequests()
		    		.anyRequest().authenticated();
            
            http
            	.requestCache()
            	.requestCache(new NullRequestCache());
            
            http
           		.exceptionHandling()
             	.authenticationEntryPoint(new RestUnauthorizedEntryPoint(objectMapper))
             	.accessDeniedHandler(new RestAccessDeniedHandler(objectMapper));

            http
            	.addFilterAfter(new RestLoginFilter("/api/authenticate", authenticationManager(), jwtService), UsernamePasswordAuthenticationFilter.class)
            	.addFilterBefore(new RestAccessFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        } catch (Exception e) {   
            LOG.error(e.getLocalizedMessage(), e);
        }

    }

}
