package com.dbs.loyalty.web.controller.error;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
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
import com.dbs.loyalty.service.LogApiService;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.ErrorResponse;
import com.dbs.loyalty.web.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;


@ControllerAdvice
public class GlobalExceptionHandler extends AbstractErrorController{

	private final LogApiService logApiService;
	
	public GlobalExceptionHandler(final ObjectMapper objectMapper, final LogApiService logApiService) {
		super(objectMapper);
		this.logApiService = logApiService;
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleError405(HttpServletRequest request, HttpServletResponse response, HttpRequestMethodNotSupportedException e) {
		return super.handleApiOrWebError(request, response);
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
    public ResponseEntity<Response> missingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request){
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new Response(ex.getMessage()));
    }
	
	@ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<Response> multipartException(MultipartException ex, HttpServletRequest request){
		logApiService.saveFileError(UrlUtil.getFullUrl(request), SecurityUtil.getCustomer(), ex.getLocalizedMessage());
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new Response(ex.getMessage()));
    }
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Response> handleException(MaxUploadSizeExceededException ex, HttpServletRequest request){
		logApiService.saveFileError(UrlUtil.getFullUrl(request), SecurityUtil.getCustomer(), ex.getLocalizedMessage());
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new Response(ex.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
		Map<String, String> errors = getErrors(ex);
		saveError(ex, request, errors);
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(errors);
	}
	
	private Map<String, String> getErrors(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
		
		for(ObjectError objectError : ex.getBindingResult().getAllErrors()) {
			FieldError fieldError = (FieldError) objectError;
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		
		return errors;
	}
	
	private void saveError(MethodArgumentNotValidException ex, HttpServletRequest request, Map<String, String> errors) {
		for(Map.Entry<String, Object> entry : ex.getBindingResult().getModel().entrySet()) {
			if(!(entry.getValue() instanceof BeanPropertyBindingResult)) {
				logApiService.saveError(UrlUtil.getFullUrl(request), entry.getKey(), SecurityUtil.getCustomer(), entry.getValue(), errors);
			}
		}
	}

}
