package com.dbs.priviledge.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import com.dbs.priviledge.security.ApiAccessFilter;
import com.dbs.priviledge.security.ApiLoginFilter;
import com.dbs.priviledge.security.JwtService;
import com.dbs.priviledge.security.RestUnauthorizedEntryPoint;

@Order(1)
@Configuration
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

	private final Logger LOG = LoggerFactory.getLogger(ApiSecurityConfig.class);
	
    private RestUnauthorizedEntryPoint restUnauthorizedEntryPoint;

    private AccessDeniedHandler accessDeniedHandler;
    
    private JwtService jwtService;

    public ApiSecurityConfig(RestUnauthorizedEntryPoint restUnauthorizedEntryPoint, AccessDeniedHandler accessDeniedHandler, JwtService jwtService) {
    	this.restUnauthorizedEntryPoint = restUnauthorizedEntryPoint;
		this.accessDeniedHandler = accessDeniedHandler;
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
             	.authenticationEntryPoint(restUnauthorizedEntryPoint)
             	.accessDeniedHandler(accessDeniedHandler);

            http
            	.addFilterAfter(new ApiLoginFilter("/api/authenticate", authenticationManager(), jwtService), UsernamePasswordAuthenticationFilter.class)
            	.addFilterBefore(new ApiAccessFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        } catch (Exception e) {   
            LOG.error(e.getLocalizedMessage(), e);
        }

    }

}
