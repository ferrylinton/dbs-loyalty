package com.dbs.priviledge.security;

import java.io.IOException;
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

import com.dbs.priviledge.util.JsonUtil;
import com.dbs.priviledge.util.ResponseUtil;


public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger LOG = LoggerFactory.getLogger(ApiLoginFilter.class);

    private JsonUtil jsonUtil = JsonUtil.getInstance();

	public ApiLoginFilter(String url, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
		Map<?, ?> credentials = jsonUtil.getObjectMapper().readValue(httpServletRequest.getInputStream(), Map.class);
		ApiAuthentication authentication = new ApiAuthentication(credentials.get("email"), credentials.get("password"));
        return getAuthenticationManager().authenticate(authentication);
    }

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authentication);
		ApiAuthentication apiAuthentication = (ApiAuthentication) authentication;
		
		ResponseUtil.sendResponse(response, jsonUtil.getObjectMapper().writeValueAsString(apiAuthentication.getUser()));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		LOG.error(failed.getLocalizedMessage(), failed);
		ResponseUtil.sendResponse(request, response, JsonUtil.getInstance().getObjectMapper(), "Authentication failed", failed.getLocalizedMessage(), HttpServletResponse.SC_UNAUTHORIZED);
	}

}
