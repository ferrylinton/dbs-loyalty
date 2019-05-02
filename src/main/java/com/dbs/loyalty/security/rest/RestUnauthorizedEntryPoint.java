package com.dbs.loyalty.security.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.dbs.loyalty.config.constant.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestUnauthorizedEntryPoint implements AuthenticationEntryPoint {

	private static final String UNAUTHORIZED = "Unauthorized";
	
	private ObjectMapper objectMapper;

	public RestUnauthorizedEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    	String message = (String) request.getAttribute(Constant.MESSAGE);
    	message = message == null ? UNAUTHORIZED : message;
    	
    	Map<String, Object> result = new HashMap<>();
        result.put(Constant.MESSAGE, message);
    	result.put(Constant.REQUEST_URI, request.getRequestURI());

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
