package com.dbs.priviledge.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.model.BadRequestResponse;
import com.dbs.priviledge.model.HtmlResponse;
import com.dbs.priviledge.model.SuccessResponse;
import com.dbs.priviledge.service.MessageService;

public class ResponseUtil {
	
	private static final String IMAGE_SAVE = "message.image";
	
	private static final String MESSAGE_SAVE = "message.save";
	
	private static final String MESSAGE_DELETE = "message.delete";
	
	public static ResponseEntity<?> createImageResponse(String html) {
		String message = MessageService.getMessage(IMAGE_SAVE);
		return createHtmlResponse(message, html);
	}
	
	public static ResponseEntity<?> createHtmlResponse(String message, String html) {
		HtmlResponse response = new HtmlResponse();
		response.setMessage(message);
		response.setHtml(html);
		return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(response);
	}
	
	public static ResponseEntity<?> createSaveResponse(String data, String entityName) {
		String message = MessageService.getMessage(MESSAGE_SAVE, data);
		String resultUrl = UrlUtil.getEntityUrl(entityName);
		return createSuccessResponse(message, resultUrl);
	}
	
	public static ResponseEntity<?> createDeleteResponse(String data, String entityName) {
		String message = MessageService.getMessage(MESSAGE_DELETE, data);
		String resultUrl = UrlUtil.getEntityUrl(entityName);
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
	
	public static ResponseEntity<?> createBadRequestResponse(String field, String message) {
		BadRequestResponse response = new BadRequestResponse();
		response.getFields().add(field);
		response.setMessage(message);
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	public static ResponseEntity<?> createBadRequestResponse(Exception ex) {
		BadRequestResponse response = new BadRequestResponse();
		response.setMessage(ErrorUtil.getErrorMessage(ex));
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
}
