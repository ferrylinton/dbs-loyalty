package com.dbs.priviledge.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.dbs.priviledge.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestUnauthorizedEntryPoint implements AuthenticationEntryPoint {

	private static final Logger LOG = LoggerFactory.getLogger(RestUnauthorizedEntryPoint.class);
	
	private ObjectMapper objectMapper;

	public RestUnauthorizedEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    	LOG.error(exception.getLocalizedMessage(), exception);
    	ResponseUtil.sendResponse(request, response, objectMapper, "Unauthorized", exception.getLocalizedMessage(), HttpServletResponse.SC_UNAUTHORIZED);
    }
}
