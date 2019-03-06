package com.dbs.loyalty.web.controller.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.dbs.loyalty.model.BadRequestResponse;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.util.ErrorUtil;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final String FILE_SIZE_IS_NOT_VALID = "message.fileSizeIsNotValid";
	
	private final String FILE = "file";
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleException(DataIntegrityViolationException ex){
		BadRequestResponse response = new BadRequestResponse();
		response.setMessage(ErrorUtil.getErrorMessage(ex));
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<?> handleException(MaxUploadSizeExceededException ex){
		BadRequestResponse response = new BadRequestResponse();
		response.getFields().add(FILE);
		response.setMessage(MessageService.getMessage(FILE_SIZE_IS_NOT_VALID));
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
}
