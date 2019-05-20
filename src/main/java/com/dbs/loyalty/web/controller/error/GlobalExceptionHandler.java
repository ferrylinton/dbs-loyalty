package com.dbs.loyalty.web.controller.error;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.ErrorData;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.web.response.ErrorResponse;
import com.dbs.loyalty.web.response.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String ERROR_DATA = "errorData";
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleError405(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
		log.error(e.getLocalizedMessage(), e);
		return handleWebError(new ErrorData(request), request);
    }
	
	private String handleWebError(ErrorData errorData, HttpServletRequest request) {
		request.setAttribute(ERROR_DATA, errorData);
		return "error/error-view";
	}
	
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
	
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException ex){
       return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new ErrorResponse(ex.getLocalizedMessage()));
    }
	
	@ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException(BadCredentialsException ex){
       return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(new ErrorResponse(ex.getLocalizedMessage()));
    }
	
	@ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Response> badRequestException(BadRequestException ex){
       return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new Response(ex.getMessage()));
    }
	
	@ExceptionHandler(value = MissingServletRequestPartException.class)
    public ResponseEntity<Response> missingServletRequestPartException(MissingServletRequestPartException ex){
       return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new Response(ex.getMessage()));
    }
	
	@ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<Response> multipartException(MultipartException ex){
       return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new Response(ex.getMessage()));
    }
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Response> handleException(MaxUploadSizeExceededException ex){
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new Response(ex.getMessage()));
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
