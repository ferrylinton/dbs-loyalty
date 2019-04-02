package com.dbs.loyalty.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {

	public static void sendResponse(HttpServletRequest request, HttpServletResponse response, ObjectMapper objectMapper, String error, String message, int status) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", new Date());
        result.put("status", status);
        result.put("error", error);
        result.put("message", message);
    	result.put("path", request.getRequestURI());

        setCors(response);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
	
	public static void sendResponse(HttpServletResponse response, String message) throws IOException {
        setCors(response);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        writer.write(message);
        writer.flush();
        writer.close();
    }
	
	public static void setCors(HttpServletResponse response) {
    	response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, x-auth-token");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, x-auth-token");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 3600);    	
    }
	
}
