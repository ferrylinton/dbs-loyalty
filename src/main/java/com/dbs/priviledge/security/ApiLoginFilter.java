package com.dbs.priviledge.security;

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

import com.dbs.priviledge.model.Login;
import com.dbs.priviledge.util.JsonUtil;
import com.dbs.priviledge.util.ResponseUtil;


public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger LOG = LoggerFactory.getLogger(ApiLoginFilter.class);

    private JsonUtil jsonUtil = JsonUtil.getInstance();
    
    private JwtService jwtService;

	public ApiLoginFilter(String url, AuthenticationManager authenticationManager, JwtService jwtService) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			Login login = jsonUtil.getObjectMapper().readValue(request.getInputStream(), Login.class);
	        return getAuthenticationManager().authenticate(new ApiAuthentication(login));
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
		
    }

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authentication);
		ApiAuthentication apiAuthentication = (ApiAuthentication) authentication;
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
