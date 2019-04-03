package com.dbs.loyalty.web.controller.error;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.dbs.loyalty.model.BadRequestResponse;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.util.ErrorUtil;

@ControllerAdvice
public class GlobalExceptionHandler {

	private String fileSizeIsNotValid = "message.fileSizeIsNotValid";
	
	private String file = "file";
	
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> methodNotSupportErrorHandler(HttpServletRequest request, Exception ex){
        ErrorResponse response = new ErrorResponse(ex.getLocalizedMessage());
        return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(response);
    }
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<BadRequestResponse> handleException(DataIntegrityViolationException ex){
		BadRequestResponse response = new BadRequestResponse();
		response.setMessage(ErrorUtil.getErrorMessage(ex));
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<BadRequestResponse> handleException(MaxUploadSizeExceededException ex){
		BadRequestResponse response = new BadRequestResponse();
		response.getFields().add(file);
		response.setMessage(MessageService.getMessage(fileSizeIsNotValid));
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<String, String>();
		
		for(ObjectError objectError : ex.getBindingResult().getAllErrors()) {
			FieldError fieldError = (FieldError) objectError;
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(errors);
	}
	
}
