package com.dbs.loyalty.web.controller.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.loyalty.exception.AbstractException;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.util.ErrorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
	
	private final String STATUS_CODE 	= "statusCode";
	
	private final String REQUEST_URI	= "requestURI";
	
	private final String MESSAGE 		= "message";
	
	private final String DETAIL 		= "detail";
	
	private final String TEMPLATE		= "error/view";
	
	private final String MESSAGE_404	= "Resource not found";
	
	private final String ACCEPT			= "Accept";
	
	private final String HTML			= "html";
	
	private ObjectMapper objectMapper;

	public ErrorController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@GetMapping("/error")
	public String handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		String requestURI = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		int statusCode =  (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String message = null;
		
		if(statusCode == 404) {
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
		
		if(request.getHeader(ACCEPT) == null || HTML.contains(request.getHeader(ACCEPT))) {
			request.setAttribute(STATUS_CODE, statusCode);
			request.setAttribute(REQUEST_URI, requestURI);
			request.setAttribute(MESSAGE, message);

		}else {
			Map<String, Object> result = new HashMap<>();
			result.put(STATUS_CODE, statusCode);
			result.put(REQUEST_URI, requestURI);
			result.put(MESSAGE, message);
			
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	        response.setStatus(statusCode);
	        PrintWriter writer = response.getWriter();
	        writer.write(objectMapper.writeValueAsString(result));
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
