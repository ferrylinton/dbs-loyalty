package com.dbs.priviledge.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.dbs.priviledge.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestAccessDeniedHandler.class);
    
	private ObjectMapper objectMapper;

	public RestAccessDeniedHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
    	LOG.error(exception.getLocalizedMessage(), exception);
    	ResponseUtil.sendResponse(request, response, objectMapper, "Forbidden", exception.getLocalizedMessage(), HttpServletResponse.SC_FORBIDDEN);
    }
    
}