package com.dbs.loyalty.web.controller.error;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dbs.loyalty.exception.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.web.response.BadRequestResponse;
import com.dbs.loyalty.web.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private String fileSizeIsNotValid = "message.fileSizeIsNotValid";
	
	private String file = "file";
	
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> methodNotSupportErrorHandler(HttpServletRequest request, Exception ex){
       return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(new ErrorResponse(ex));
    }
	
	@ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException ex){
       return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(new ErrorResponse(ex.getLocalizedMessage()));
    }
	
	@ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException(BadCredentialsException ex){
       return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(new ErrorResponse(ex.getLocalizedMessage()));
    }
	
	@ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<BadRequestResponse> badRequestException(BadRequestException ex){
       return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(ex.getResponse());
    }
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<BadRequestResponse> handleException(DataIntegrityViolationException ex){
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new BadRequestResponse(ex.getLocalizedMessage()));
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<BadRequestResponse> handleException(MaxUploadSizeExceededException ex){
		String message = MessageService.getMessage(fileSizeIsNotValid);
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new BadRequestResponse(message, file));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		
		for(ObjectError objectError : ex.getBindingResult().getAllErrors()) {
			FieldError fieldError = (FieldError) objectError;
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(errors);
	}
	
}
