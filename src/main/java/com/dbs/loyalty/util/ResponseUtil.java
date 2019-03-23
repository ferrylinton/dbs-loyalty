package com.dbs.loyalty.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.model.BadRequestResponse;
import com.dbs.loyalty.model.SuccessResponse;
import com.dbs.loyalty.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {
	
	private static final String MESSAGE_SAVE = "message.save";
	
	private static final String MESSAGE_DELETE = "message.delete";
	
	public static ResponseEntity<?> createSaveResponse(String data, String entityName) {
		String message = MessageService.getMessage(MESSAGE_SAVE, data);
		String resultUrl = UrlUtil.getUrl(entityName);
		return createSuccessResponse(message, resultUrl);
	}
	
	public static ResponseEntity<?> createDeleteResponse(String data, String entityName) {
		String message = MessageService.getMessage(MESSAGE_DELETE, data);
		String resultUrl = UrlUtil.getUrl(entityName);
		return createSuccessResponse(message, resultUrl);
	}
	
	public static ResponseEntity<?> createSuccessResponse(String message, String resultUrl) {
		SuccessResponse response = new SuccessResponse();
		response.setMessage(message);
		response.setResultUrl(resultUrl);
		return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(response);
	}
	
	public static ResponseEntity<?> createBadRequestResponse(BindingResult result) {
		BadRequestResponse response = new BadRequestResponse();
		String message = Constant.EMPTY;
		
		for (FieldError fieldError : result.getFieldErrors()) {
			response.getFields().add(fieldError.getField());
			message += fieldError.getDefaultMessage() + Constant.BR; 
		}

		response.setMessage(message);
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
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
