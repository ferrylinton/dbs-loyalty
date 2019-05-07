package com.dbs.loyalty.security.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.dbs.loyalty.web.response.ForbiddenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;


public class RestAccessDeniedHandler implements AccessDeniedHandler {

	private ObjectMapper objectMapper;

	public RestAccessDeniedHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
    	response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(403);
        
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(new ForbiddenResponse(request)));
        writer.flush();
        writer.close();
    }
    
}