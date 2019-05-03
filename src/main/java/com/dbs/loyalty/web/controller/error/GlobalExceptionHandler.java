package com.dbs.loyalty.web.controller.error;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.web.response.BadRequestResponse;
import com.dbs.loyalty.web.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private String fileSizeIsNotValid = "message.fileSizeIsNotValid";

	private String file = "file";
	
	@ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> methodNotSupportErrorHandler(ConstraintViolationException ex){
       return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorResponse(ErrorUtil.getThrowable(ex).getMessage()));
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
	
	@ExceptionHandler(value = MissingServletRequestPartException.class)
    public ResponseEntity<BadRequestResponse> missingServletRequestPartException(MissingServletRequestPartException ex){
       return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new BadRequestResponse(ex.getLocalizedMessage()));
    }
	
	@ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<BadRequestResponse> multipartException(MultipartException ex){
       return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new BadRequestResponse(ex.getLocalizedMessage()));
    }
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<BadRequestResponse> handleException(MaxUploadSizeExceededException ex){
		String message = MessageUtil.getMessage(fileSizeIsNotValid);
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
