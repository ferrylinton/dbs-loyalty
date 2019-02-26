package com.dbs.priviledge.web.controller.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.priviledge.exception.AbstractException;
import com.dbs.priviledge.service.MessageService;
import com.dbs.priviledge.util.ErrorUtil;
import com.dbs.priviledge.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
	
	private static final String STATUS_CODE 		= "statusCode";
	
	private static final String REQUEST_URI			= "requestURI";
	
	private static final String MESSAGE 			= "message";
	
	private static final String DETAIL 				= "detail";
	
	private static final String TEMPLATE			= "error/view";
	
	private static final String MESSAGE_400			= "Bad Request";
	
	private static final String MESSAGE_401			= "Unauthorized";
	
	private static final String MESSAGE_403			= "Forbidden";
	
	private static final String MESSAGE_404			= "Resource not found";

	@GetMapping("/error")
	public String handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("---------- Accept : " + request.getHeader("Accept"));
		
		if(request.getHeader("Accept") == null || request.getHeader("Accept").contains("html")) {
			Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
			String requestURI = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
			int statusCode =  (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			String message = null;
			
			if(statusCode == 400) {
				message = MESSAGE_400;
			}else if(statusCode == 401) {
				message = MESSAGE_401;
			}else if(statusCode == 403) {
				message = MESSAGE_403;
			}else if(statusCode == 404) {
				message = MESSAGE_404;
			}else if(statusCode == 501) {
				message = exception.getMessage();
			}else if(statusCode == 500 && exception != null) {
				
				Exception ex = (Exception) ErrorUtil.getThrowable(exception);
				
				if(ex instanceof AbstractException) {
					AbstractException abstractException = (AbstractException) ex;
					message = MessageService.getMessage(abstractException.getMessage());
					statusCode = abstractException.getStatus();
					response.setStatus(abstractException.getStatus());
				}else {
					message = exception.getMessage();
				}
				
				request.setAttribute(DETAIL, ExceptionUtils.getStackTrace(exception));
			}
			
			request.setAttribute(STATUS_CODE, statusCode);
			request.setAttribute(REQUEST_URI, requestURI);
			request.setAttribute(MESSAGE, message);

		}else {
			Map<String, Object> result = new HashMap<>();
			result.put("message", "test");
			response.setContentType("application/json;charset=UTF-8");
	        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	        PrintWriter writer = response.getWriter();
	        writer.write(JsonUtil.getInstance().getObjectMapper().writeValueAsString(result));
	        writer.flush();
	        writer.close();
		}
		
		return TEMPLATE;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
