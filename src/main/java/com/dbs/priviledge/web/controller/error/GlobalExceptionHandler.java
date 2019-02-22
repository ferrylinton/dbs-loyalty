package com.dbs.priviledge.web.controller.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.dbs.priviledge.service.MessageService;
import com.dbs.priviledge.util.ResponseUtil;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final String FILE_SIZE_IS_NOT_VALID = "message.fileSizeIsNotValid";
	
	private final String FILE = "file";
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleException(DataIntegrityViolationException ex){
		return ResponseUtil.createBadRequestResponse(ex);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<?> handleException(MaxUploadSizeExceededException ex){
		return ResponseUtil.createBadRequestResponse(FILE, MessageService.getMessage(FILE_SIZE_IS_NOT_VALID));
	}
	
}
