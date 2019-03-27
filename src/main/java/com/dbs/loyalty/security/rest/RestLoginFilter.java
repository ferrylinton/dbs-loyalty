package com.dbs.loyalty.security.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dbs.loyalty.service.dto.JWTLoginDto;
import com.dbs.loyalty.util.JsonUtil;
import com.dbs.loyalty.util.ResponseUtil;


public class RestLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger LOG = LoggerFactory.getLogger(RestLoginFilter.class);

    private JsonUtil jsonUtil = JsonUtil.getInstance();
    
    private RestTokenProvider jwtService;

	public RestLoginFilter(String url, AuthenticationManager authenticationManager, RestTokenProvider jwtService) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			JWTLoginDto login = jsonUtil.getObjectMapper().readValue(request.getInputStream(), JWTLoginDto.class);
	        return getAuthenticationManager().authenticate(new RestAuthentication(login));
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
		
    }

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authentication);
		RestAuthentication apiAuthentication = (RestAuthentication) authentication;
		Map<String, Object> result = new HashMap<>();
		result.put("token", jwtService.createToken(apiAuthentication, false));
		result.put("data", apiAuthentication.getCustomer());
		
		ResponseUtil.sendResponse(response, jsonUtil.getObjectMapper().writeValueAsString(result));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		LOG.error(failed.getLocalizedMessage(), failed);
		ResponseUtil.sendResponse(request, response, JsonUtil.getInstance().getObjectMapper(), "Authentication failed", failed.getLocalizedMessage(), HttpServletResponse.SC_UNAUTHORIZED);
	}

}
