package com.dbs.loyalty.security.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.dbs.loyalty.web.response.UnauthorizedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestUnauthorizedEntryPoint implements AuthenticationEntryPoint {
	
	private ObjectMapper objectMapper;

	public RestUnauthorizedEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    	response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(401);
        
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(new UnauthorizedResponse(request)));
        writer.flush();
        writer.close();
    }
    
}
